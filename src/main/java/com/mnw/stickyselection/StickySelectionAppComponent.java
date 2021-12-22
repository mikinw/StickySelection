package com.mnw.stickyselection;

import com.intellij.AppTopics;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.fileEditor.FileDocumentManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.ColorIcon;
import com.mnw.stickyselection.actions.ClearPaintGroupInstantAction;
import com.mnw.stickyselection.actions.ConvertPaintGroupInstantAction;
import com.mnw.stickyselection.actions.PaintSelectionInstantAction;
import com.mnw.stickyselection.actions.StickyEditorAction;
import com.mnw.stickyselection.model.ValuesRepository;
import com.mnw.stickyselection.model.ValuesRepositoryImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StickySelectionAppComponent implements StartupActivity, Disposable {

    private static final String PLUGIN_ACTION_ID = "com.mnw.stickyselection";
    private static final String ACTION_ID_PREFIX = "com.mnw.stickyselection.actions.";
    private static final int ACTION_ICON_SIZE = 12;
    private final Map<Editor, StickySelectionEditorComponent> editors = new HashMap<>();

    public StickySelectionAppComponent() {
        ApplicationManager.getApplication().getMessageBus().connect().subscribe(AppTopics.FILE_DOCUMENT_SYNC, new FileDocumentManagerListener() {
            @Override
            public void beforeAllDocumentsSaving() {
                for (StickySelectionEditorComponent editorComponent : editors.values()) {
                    editorComponent.persistHighlights();
                }
                editors.values().forEach(StickySelectionEditorComponent::persistHighlights);
            }

            @Override
            public void beforeDocumentSaving(@NotNull Document document) {
                for (StickySelectionEditorComponent stickySelectionEditorComponent : editors.values()) {
                    stickySelectionEditorComponent.persistHighlights(document);
                }
            }

            @Override
            public void beforeFileContentReload(VirtualFile virtualFile, @NotNull Document document) {}
            @Override
            public void fileWithNoDocumentChanged(@NotNull VirtualFile virtualFile) {}
            @Override
            public void fileContentReloaded(@NotNull VirtualFile virtualFile, @NotNull Document document) {}
            @Override
            public void fileContentLoaded(@NotNull VirtualFile virtualFile, @NotNull Document document) {}
            @Override
            public void unsavedDocumentsDropped() {}
        });
    }

    @Override
    public void runActivity(@NotNull final Project project) {
        updateRegisteredActions();

    }


    public void updateAllHighlighters() {
        editors.values().forEach(StickySelectionEditorComponent::updateAllHighlighters);
    }

    public void updateRegisteredActions() {
        final int paintGroupCount = ValuesRepositoryImpl.getInstance().getPaintGroupCount();
        final ActionManager actionManager = ActionManager.getInstance();

        final String paintSelectionInstantAction = "PaintSelectionInstantAction.";
        final String clearPaintGroupInstantAction = "ClearPaintGroupInstantAction.";
        final String convertPaintGroupInstantAction = "ConvertPaintGroupInstantAction.";
        final List<String> paintActionIds = actionManager.getActionIdList(ACTION_ID_PREFIX + paintSelectionInstantAction);
        final List<String> clearActionIds = actionManager.getActionIdList(ACTION_ID_PREFIX + clearPaintGroupInstantAction);
        final List<String> convertActionIds = actionManager.getActionIdList(ACTION_ID_PREFIX + convertPaintGroupInstantAction);

        updateActionsOfSpecificType(paintGroupCount,
                                    actionManager,
                                    paintSelectionInstantAction,
                                    paintActionIds,
                                    new InstantActionFactory() {
                                        @NotNull
                                        @Override
                                        public StickyEditorAction createInstantAction(int paintGroup, Icon actionIcon) {
                                            return new PaintSelectionInstantAction(paintGroup, actionIcon);
                                        }
                                    });
        updateActionsOfSpecificType(paintGroupCount,
                                    actionManager,
                                    clearPaintGroupInstantAction,
                                    clearActionIds,
                                    new InstantActionFactory() {
                                        @NotNull
                                        @Override
                                        public StickyEditorAction createInstantAction(int paintGroup, Icon actionIcon) {
                                            return new ClearPaintGroupInstantAction(paintGroup, actionIcon);
                                        }
                                    });
        updateActionsOfSpecificType(paintGroupCount,
                                    actionManager,
                                    convertPaintGroupInstantAction,
                                    convertActionIds,
                                    new InstantActionFactory() {
                                        @NotNull
                                        @Override
                                        public StickyEditorAction createInstantAction(int paintGroup, Icon actionIcon) {
                                            return new ConvertPaintGroupInstantAction(paintGroup, actionIcon);
                                        }
                                    });

    }

    private void updateActionsOfSpecificType(int paintGroupCount, ActionManager actionManager,
                                             String instantAction, List<String> actionIds, InstantActionFactory instantActionFactory) {
        final int length = actionIds.size();
        for (int i = 0; i < Math.min(paintGroupCount, length); i++) {
            final AnAction action = actionManager.getAction(ACTION_ID_PREFIX + instantAction + i);
            if (action != null) {
                final Icon currentIcon = action.getTemplatePresentation().getIcon();
                ValuesRepository savedValues = ValuesRepositoryImpl.getInstance();
                if (currentIcon instanceof ColorIcon) {
                    if (!((ColorIcon)currentIcon).getIconColor().equals(savedValues.getPaintGroupProperties(i).getColor())) {
                        action.getTemplatePresentation().setIcon(createActionIcon(i));
                    }
                }
            }
        }
        for (int i = length; i < paintGroupCount; i++) {
            actionManager.registerAction(
                    ACTION_ID_PREFIX + instantAction + i,
                    instantActionFactory.createInstantAction(i, createActionIcon(i)),
                    PluginId.getId(PLUGIN_ACTION_ID));
        }
        for (int i = length - 1; i >= paintGroupCount; i--) {
            actionManager.unregisterAction(ACTION_ID_PREFIX + instantAction + i);
        }
    }

    @NotNull
    private Icon createActionIcon(int i) {
        ValuesRepository savedValues = ValuesRepositoryImpl.getInstance();
        final Color color = savedValues.getPaintGroupProperties(i).getColor();
        return new ColorIcon(ACTION_ICON_SIZE, color);
    }

    public StickySelectionEditorComponent getStickySelectionEditorComponent(Editor editor) {
        return editors.get(editor);
    }

    @Override
    public void dispose() {
        editors.forEach((e, se) -> se.dispose());
        editors.clear();

    }

    void editorReleased(final Editor editor) {
        StickySelectionEditorComponent editorHighlighter = editors.remove(editor);
        if(editorHighlighter == null) {
            return;
        }

        Disposer.dispose(editorHighlighter);

    }

    public void editorCreated(final Editor editor, final StickySelectionEditorComponent editorHighlighter) {
        editors.put(editor, editorHighlighter);
    }

    private interface InstantActionFactory {
        @NotNull
        StickyEditorAction createInstantAction(int paintGroup, Icon actionIcon);
    }
}

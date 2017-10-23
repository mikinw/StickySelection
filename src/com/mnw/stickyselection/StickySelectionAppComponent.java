package com.mnw.stickyselection;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.ColorIcon;
import com.mnw.stickyselection.actions.ClearPaintGroupInstantAction;
import com.mnw.stickyselection.actions.ConvertPaintGroupInstantAction;
import com.mnw.stickyselection.actions.PaintSelectionInstantAction;
import com.mnw.stickyselection.actions.StickyEditorAction;
import com.mnw.stickyselection.model.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class StickySelectionAppComponent implements ApplicationComponent, EditorFactoryListener, Disposable {

    private static final String PLUGIN_ACTION_ID = "com.mnw.stickyselection";
    private static final String ACTION_ID_PREFIX = "com.mnw.stickyselection.actions.";
    private static final int ACTION_ICON_SIZE = 12;
    private HashMap<Editor, StickySelectionEditorComponent> editors = new HashMap<>();

    private ValuesRepository savedValues;


    @Override
    public void initComponent() {
        savedValues = ServiceManager.getService(ValuesRepository.class);

        //Add listener for editors
        EditorFactory.getInstance().addEditorFactoryListener(this, this);

        updateRegisteredActions();

    }

    @Override
    public void disposeComponent() {
        //Remove listener for editors
        editors.clear();
    }

    @NotNull
    @Override
    public String getComponentName() {
        return("StickySelectionAppComponent");
    }

    @Override
    public void editorCreated(@NotNull EditorFactoryEvent editorFactoryEvent) {
        Editor editor = editorFactoryEvent.getEditor();

        StickySelectionEditorComponent editorHighlighter = new StickySelectionEditorComponent(editor);
        editors.put(editorFactoryEvent.getEditor(), editorHighlighter);

        final Project project = editor.getProject();
        if (project == null) {
            return;
        }

        final StoredHighlightsRepository projectSettings = ServiceManager
                .getService(project, StoredHighlightsRepository.class);

        if (ServiceManager.getService(ValuesRepository.class).getPersistHighlights()) {
            final VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
            if (file == null) {
                return;
            }
            final String path = file.getPath();

            final Map<Integer, EditorHighlightsForPaintGroup> editorHighlights = projectSettings
                    .getEditorHighlights(path);


            editorHighlighter.loadHighlights(editorHighlights);
        } else {
            projectSettings.clear();
        }


    }

    @Override
    public void editorReleased(@NotNull EditorFactoryEvent editorFactoryEvent) {
        // TODO: 2017. 10. 23. do I need to save the whole (not just the diff, like during actions), to make sure things are saved correctly?
//        Editor editor = editorFactoryEvent.getEditor();
//        final Project project = editor.getProject();
//        if (project == null) {
//            return;
//        }
//        final StoredHighlightsRepository projectSettings = ServiceManager.getService(project, StoredHighlightsRepository.class);


        StickySelectionEditorComponent editorHighlighter = editors.remove(editorFactoryEvent.getEditor());
        if(editorHighlighter == null) {
            return;
        }

//        final VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
//        if (file == null) {
//            return;
//        }
//        final String path = file.getPath();

        //projectSettings.addOrUpdateEditorHighlights(editorHighlighter.saveHighlights(), path);

        editorHighlighter.dispose();
    }

    public void updateAllHighlighters() {
        editors.values().forEach(StickySelectionEditorComponent::updateAllHighlighters);
    }

    public void updateRegisteredActions() {
        final int paintGroupCount = savedValues.getPaintGroupCount();
        final ActionManager actionManager = ActionManager.getInstance();

        final String paintSelectionInstantAction = "PaintSelectionInstantAction.";
        final String clearPaintGroupInstantAction = "ClearPaintGroupInstantAction.";
        final String convertPaintGroupInstantAction = "ConvertPaintGroupInstantAction.";
        final String[] paintActionIds = actionManager.getActionIds(ACTION_ID_PREFIX + paintSelectionInstantAction);
        final String[] clearActionIds = actionManager.getActionIds(ACTION_ID_PREFIX + clearPaintGroupInstantAction);
        final String[] convertActionIds = actionManager.getActionIds(ACTION_ID_PREFIX + convertPaintGroupInstantAction);

        updateActionsOfSpecificType(paintGroupCount,
                                    actionManager,
                                    paintSelectionInstantAction,
                                    paintActionIds,
                                    PaintSelectionInstantAction::new);
        updateActionsOfSpecificType(paintGroupCount,
                                    actionManager,
                                    clearPaintGroupInstantAction,
                                    clearActionIds,
                                    ClearPaintGroupInstantAction::new);
        updateActionsOfSpecificType(paintGroupCount,
                                    actionManager,
                                    convertPaintGroupInstantAction,
                                    convertActionIds,
                                    ConvertPaintGroupInstantAction::new);

    }

    private void updateActionsOfSpecificType(int paintGroupCount, ActionManager actionManager,
                                             String instantAction, String[] actionIds, InstantActionFactory instantActionFactory) {
        final int length = actionIds.length;
        for (int i = 0; i < Math.min(paintGroupCount, length); i++) {
            final AnAction action = actionManager.getAction(ACTION_ID_PREFIX + instantAction + i);
            if (action != null) {
                final Icon currentIcon = action.getTemplatePresentation().getIcon();
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
        final Color color = savedValues.getPaintGroupProperties(i).getColor();
        return new ColorIcon(ACTION_ICON_SIZE, color);
    }

    public StickySelectionEditorComponent getStickySelectionEditorComponent(Editor editor) {
        return editors.get(editor);
    }

    @Override
    public void dispose() {
        disposeComponent();
    }

    private interface InstantActionFactory {
        @NotNull
        StickyEditorAction createInstantAction(int paintGroup, Icon actionIcon);
    }
}

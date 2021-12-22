package com.mnw.stickyselection;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.mnw.stickyselection.model.EditorHighlightsForPaintGroup;
import com.mnw.stickyselection.model.StoredHighlightsRepository;
import com.mnw.stickyselection.model.ValuesRepositoryImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class StickySelectionEditorFactoryListener implements com.intellij.openapi.editor.event.EditorFactoryListener {

    @Override
    public void editorCreated(@NotNull EditorFactoryEvent editorFactoryEvent) {
        final Editor editor = editorFactoryEvent.getEditor();

        StickySelectionAppComponent appComponent = ApplicationManager.getApplication().getComponent(StickySelectionAppComponent.class);

        StickySelectionEditorComponent editorHighlighter = new StickySelectionEditorComponent(editor);

        appComponent.editorCreated(editorFactoryEvent.getEditor(), editorHighlighter);

        final Project project = editor.getProject();
        if (project == null) {
            return;
        }

        final StoredHighlightsRepository projectSettings = project.getService(StoredHighlightsRepository.class);

        if (ValuesRepositoryImpl.getInstance().getPersistHighlights()) {
            final VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
            if (file == null) {
                return;
            }
            final String path = file.getPath();

            final Map<Integer, EditorHighlightsForPaintGroup> storedEditorHighlights = projectSettings
                    .getEditorHighlights(path);


            editorHighlighter.loadHighlights(storedEditorHighlights);
        } else {
            projectSettings.clear();
        }


    }

    @Override
    public void editorReleased(@NotNull EditorFactoryEvent event) {
        StickySelectionAppComponent appComponent = ApplicationManager.getApplication().getComponent(StickySelectionAppComponent.class);

        appComponent.editorReleased(event.getEditor());

    }

}

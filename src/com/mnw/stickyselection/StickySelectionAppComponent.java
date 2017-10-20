package com.mnw.stickyselection;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.mnw.stickyselection.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class StickySelectionAppComponent implements ApplicationComponent, EditorFactoryListener,
        Disposable {

    protected HashMap<Editor, StickySelectionEditorComponent> editors = new HashMap<>();

    private ValuesRepository savedValues;


    @Override
    public void initComponent() {
        savedValues = ServiceManager.getService(ValuesRepository.class);

        //Add listener for editors
        EditorFactory.getInstance().addEditorFactoryListener(this, this);

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
        if(editor.getProject() == null) {
            return;
        }

        StickySelectionEditorComponent editorHighlighter = new StickySelectionEditorComponent(editor);
        editors.put(editorFactoryEvent.getEditor(), editorHighlighter);

    }

    @Override
    public void editorReleased(@NotNull EditorFactoryEvent editorFactoryEvent) {
        StickySelectionEditorComponent editorHighlighter = editors.remove(editorFactoryEvent.getEditor());
        if(editorHighlighter == null) {
            return;
        }

        editorHighlighter.dispose();
    }

    public void updateAllHighlighters() {
        editors.values().forEach(StickySelectionEditorComponent::updateAllHighlighters);
    }

    public StickySelectionEditorComponent getStickySelectionEditorComponent(Editor editor) {
        return editors.get(editor);
    }

    @Override
    public void dispose() {
        disposeComponent();
    }
}

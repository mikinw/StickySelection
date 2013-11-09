package com.mnw.stickyselection;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.mnw.stickyselection.infrastructure.PropertiesLoaderImpl;
import com.mnw.stickyselection.infrastructure.PropertiesSaverImpl;
import com.mnw.stickyselection.model.*;
import com.mnw.stickyselection.preferences.StickySelectionPreferences;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * TODO description of this class is missing
 */
public class StickySelectionAppComponent implements ApplicationComponent, EditorFactoryListener, Configurable {

    protected HashMap<Editor, StickySelectionEditorComponent> editors = null;

    private StickySelectionPreferences form;

    private ValuesRepositoryReader savedValues;


    @Override
    public void initComponent() {
        editors = new HashMap<Editor, StickySelectionEditorComponent>();
        final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        PropertiesLoader propertiesLoader = new PropertiesLoaderImpl(propertiesComponent);
        savedValues = propertiesLoader.loadFromPermanentStorageOrDefault(new DefaultValues());

        //Add listener for editors
        EditorFactory.getInstance().addEditorFactoryListener(this);
    }

    @Override
    public void disposeComponent() {
        //Remove listener for editors
        EditorFactory.getInstance().removeEditorFactoryListener(this);
        for(StickySelectionEditorComponent value : editors.values())
            value.dispose();
        editors.clear();
    }

    @NotNull
    @Override
    public String getComponentName() {
        return("StickySelectionAppComponent");
    }

    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {
        Editor editor = event.getEditor();
        if(editor.getProject() == null) {
            return;
        }

        StickySelectionEditorComponent editorHighlighter = new StickySelectionEditorComponent(event.getEditor());
        editors.put(event.getEditor(), editorHighlighter);

    }

    @Override
    public void editorReleased(@NotNull EditorFactoryEvent event) {
        StickySelectionEditorComponent editorHighlighter = editors.remove(event.getEditor());
        if(editorHighlighter == null) {
            return;
        }

        editorHighlighter.dispose();
    }

    public void paintSelection(Editor editor, int paintGroup) {
        StickySelectionEditorComponent editorHighlighter = editors.get(editor);
        if (editorHighlighter == null) {
            return;
        }
        PaintGroupPropertiesGetter paintGroupPropertiesGetter = createPaintGroupProperties(paintGroup);
        editorHighlighter.paintSelection(paintGroupPropertiesGetter, paintGroup);
    }

    private PaintGroupPropertiesGetter createPaintGroupProperties(int paintGroup) {
        final Color colorOfSelectionGroup = savedValues.getColorOfSelectionGroup(paintGroup);
        final boolean markerEnabledForSelectionGroup = savedValues.isMarkerEnabledForSelectionGroup(paintGroup);
        final int highlightLayerOfSelectionGroup = savedValues.getHighlightLayerOfSelectionGroup(paintGroup);
        return new PaintGroupProperties(highlightLayerOfSelectionGroup, markerEnabledForSelectionGroup, colorOfSelectionGroup);
    }

    public void clearPaintGroup(Editor editor, int paintGroup) {
        StickySelectionEditorComponent editorHighlighter = editors.get(editor);
        if(editorHighlighter == null)
            return;
        editorHighlighter.clearPaintGroup(paintGroup);
    }

    // region Configurable interface

    @Nls
    @Override
    public String getDisplayName() {
        return "Sticky Selection";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if(form == null) {
            form = new StickySelectionPreferences();
        }
        return form.getMainPanel();
    }

    @Override
    public boolean isModified() {
        return form != null && form.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        if(form == null) {
            return;
        }
//        System.out.println("apply()");
        savedValues = form.getData();
        form.setData(savedValues);
        saveValuesToPermanentStorage();

        updateAllHighlighters();
    }

    private void saveValuesToPermanentStorage() {
        final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        PropertiesSaver propertiesSaver = new PropertiesSaverImpl(propertiesComponent);
        propertiesSaver.saveToPermanentStorage(savedValues);
    }

    @Override
    public void reset() {
        if (form == null) {
            return;
        }
//        System.out.println("reset()");
        ValuesRepositoryReader values = savedValues == null ? new DefaultValues() : savedValues;
        form.setData(values);
    }

    @Override
    public void disposeUIResources() {
        form = null;
    }

    //endregion

    private void updateAllHighlighters() {
        for(StickySelectionEditorComponent editor : editors.values()) {
            editor.repaint(savedValues);
        }
    }
}

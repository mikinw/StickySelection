package com.mnw.stickyselection.preferences;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import com.mnw.stickyselection.StickySelectionAppComponent;
import com.mnw.stickyselection.StickySelectionEditorComponent;
import com.mnw.stickyselection.StickySelectionSettingsComponent;
import com.mnw.stickyselection.model.PaintGroupDataBean;
import com.mnw.stickyselection.model.ValuesRepository;
import com.mnw.stickyselection.model.ValuesRepositoryImpl;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;


import java.util.List;
import javax.swing.*;

public class StickySelectionPreferences implements Configurable {

    private StickySelectionSettingsComponent settingsComponent;



    @Nullable
    @Override
    public JComponent createComponent() {
        ValuesRepository savedValues = ValuesRepositoryImpl.getInstance();

        settingsComponent = new StickySelectionSettingsComponent(savedValues);
//        setupUI();
//        deletedDataBeans.clear();


        return settingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        ValuesRepository savedValues = ValuesRepositoryImpl.getInstance();
//        System.out.println("isModified()");
        if (savedValues.getIsCycleThroughEnabled() != settingsComponent.isCycleThrough()) {
            return true;
        }

        if (savedValues.getPersistHighlights() != settingsComponent.isPersistHighlights()) {
            return true;
        }

        final List<PaintGroupDataBean> paintGroups = settingsComponent.getPaintGroups();
        final int paintGroupCount = savedValues.getPaintGroupCount();
        if (paintGroupCount != paintGroups.size()) {
            return true;
        }



        for (int i = 0; i < paintGroups.size(); i++) {

            final PaintGroupDataBean paintGroupProperties = savedValues.getPaintGroupProperties(i);

            final PaintGroupDataBean dataBeanUi = paintGroups.get(i);


            if (!paintGroupProperties.equals(dataBeanUi)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void disposeUIResources() {
        settingsComponent = null;
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return settingsComponent.getPreferredFocusedComponent();
    }

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

    @Override
    public void apply() {
        ValuesRepository savedValues = ValuesRepositoryImpl.getInstance();



//        System.out.println("apply()");
        savedValues.setIsCycleThroughEnabled(settingsComponent.isCycleThrough());
        final boolean persist = settingsComponent.isPersistHighlights();
        savedValues.setPersistHighlights(persist);

        final List<PaintGroupDataBean> paintGroups = settingsComponent.getPaintGroups();

        final List<Integer> paintGroupIds = savedValues.getPaintGroupIds();
        savedValues.removeWithIds(paintGroupIds);

        for (int i = 0; i < paintGroups.size(); i++) {
            final PaintGroupDataBean dataBeanUi = paintGroups.get(i);

            final PaintGroupDataBean paintGroupBean = savedValues.addNewPaintGroup();
            paintGroupBean.setShortcut(dataBeanUi.getShortcut());
            paintGroupBean.setFrameNeeded(dataBeanUi.isFrameNeeded());
            paintGroupBean.setHighlightLayer(dataBeanUi.getHighlightLayer());
            paintGroupBean.setMarkerNeeded(dataBeanUi.isMarkerNeeded());
            paintGroupBean.setColor(dataBeanUi.getColor());
        }


//        savedValues.updateUI();

        StickySelectionAppComponent applicationComponent = ApplicationManager
                .getApplication()
                .getComponent(StickySelectionAppComponent.class);
        applicationComponent.updateAllHighlighters();

        applicationComponent.updateRegisteredActions();

        if (persist) {

            applicationComponent.editorIterator().forEach(e2epg -> {
                final StickySelectionEditorComponent paintGroup = e2epg.getValue();

                paintGroup.persistHighlights(true);

            });


        }
    }

    @Override
    public void reset() {
        ValuesRepository settings = ValuesRepositoryImpl.getInstance();
        settingsComponent.setData(settings);

    }


}

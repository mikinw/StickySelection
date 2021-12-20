package com.mnw.stickyselection.preferences;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.mnw.stickyselection.StickySelectionAppComponent;
import com.mnw.stickyselection.StickySelectionSettingsComponent;
import com.mnw.stickyselection.infrastructure.RandomPaintGroupData;
import com.mnw.stickyselection.model.PaintGroupDataBean;
import com.mnw.stickyselection.model.ValuesRepository;
import com.mnw.stickyselection.model.ValuesRepositoryImpl;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
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
//            refreshWarning.setVisible(false);
            return true;
        }

        if (savedValues.getPersistHighlights() != settingsComponent.isPersistHighlights()) {
//            refreshWarning.setVisible(false);
            return true;
        }

        final List<PaintGroupDataBean> paintGroups = settingsComponent.getPaintGroups();
        final int paintGroupCount = savedValues.getPaintGroupCount();
        if (paintGroupCount != paintGroups.size()) {
//            refreshWarning.setVisible(false);
            return true;
        }



        for (int i = 0; i < paintGroups.size(); i++) {

            final PaintGroupDataBean paintGroupProperties = savedValues.getPaintGroupProperties(i);

            final PaintGroupDataBean dataBeanUi = paintGroups.get(i);


            if (!paintGroupProperties.equals(dataBeanUi)) {
//                refreshWarning.setVisible(false);
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
    public void apply() throws ConfigurationException {
        ValuesRepository savedValues = ValuesRepositoryImpl.getInstance();



//        System.out.println("apply()");
        savedValues.setIsCycleThroughEnabled(settingsComponent.isCycleThrough());
        savedValues.setPersistHighlights(settingsComponent.isPersistHighlights());

//        refreshWarning.setVisible(true);
        final List<PaintGroupDataBean> paintGroups = settingsComponent.getPaintGroups();
        final int storedPaintGroupCount = savedValues.getPaintGroupCount();

        final List<Integer> paintGroupIds = savedValues.getPaintGroupIds();
        savedValues.removeWithIds(paintGroupIds);

        for (int i = 0; i < paintGroups.size(); i++) {
            final PaintGroupDataBean dataBeanUi = paintGroups.get(i);

            final PaintGroupDataBean paintGroupBean = savedValues.addNewPaintGroup();
//                paintGroupRow.linkToDataBean(savedValues.getLast());
            paintGroupBean.setShortcut(dataBeanUi.getShortcut());
            paintGroupBean.setFrameNeeded(dataBeanUi.isFrameNeeded());
            paintGroupBean.setLayer(dataBeanUi.getHighlightLayer());
            paintGroupBean.setMarkerNeeded(dataBeanUi.isMarkerNeeded());
            paintGroupBean.setColor(dataBeanUi.getColor());
        }


//        savedValues.updateUI();

        StickySelectionAppComponent applicationComponent = ApplicationManager
                .getApplication()
                .getComponent(StickySelectionAppComponent.class);
        applicationComponent.updateAllHighlighters();

        applicationComponent.updateRegisteredActions();
    }

    @Override
    public void reset() {
        ValuesRepository settings = ValuesRepositoryImpl.getInstance();
        settingsComponent.setData(settings);

    }


}

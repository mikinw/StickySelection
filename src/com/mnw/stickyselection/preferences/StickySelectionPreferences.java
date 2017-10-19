package com.mnw.stickyselection.preferences;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.mnw.stickyselection.StickySelectionAppComponent;
import com.mnw.stickyselection.infrastructure.PropertiesSaverImpl;
import com.mnw.stickyselection.model.PaintGroupDataBean;
import com.mnw.stickyselection.model.PropertiesSaver;
import com.mnw.stickyselection.model.ValuesRepository;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;

public class StickySelectionPreferences implements Configurable {

    private java.util.List<PaintGroupRow> paintGroupRows = new ArrayList<>();

    private JPanel mainPanel;

    private JButton buttonAddSelectionGroup;

    private JPanel panelColorScheme;


    private ValuesRepository savedValues;

    public StickySelectionPreferences() {
        savedValues = ServiceManager.getService(ValuesRepository.class);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setData(ValuesRepository savedValues) {
        this.savedValues = savedValues;
        while (panelColorScheme.getComponentCount() > 1) {

            panelColorScheme.remove(1);
        }
        for (int i = 0; i < savedValues.getPaintGroupCount(); i++) {
            final PaintGroupDataBean paintGroupProperties = savedValues.getPaintGroupProperties(i);
            addNewPaintGroupRow(paintGroupProperties);

            //setDataForSelectionGroupFrom(savedValues, i);
        }
    }

    private void addNewPaintGroupRow(PaintGroupDataBean paintGroupProperties) {
        final PaintGroupRow paintGroupRow = new PaintGroupRow();
        if (paintGroupProperties != null) {
            paintGroupRow.setData(paintGroupProperties);
        }
        paintGroupRow.addRemoveClickListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String actionCommand = e.getActionCommand();
                if (actionCommand.equals("RemovePaintGroup")) {
                    panelColorScheme.remove(paintGroupRow.$$$getRootComponent$$$());
                    panelColorScheme.updateUI();
                }
                paintGroupRows.remove(paintGroupRow);
            }
        });
        panelColorScheme.add(paintGroupRow.$$$getRootComponent$$$());
        paintGroupRows.add(paintGroupRow);
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        setupUI();
        setData(savedValues);
        return mainPanel;
    }

    @Override
    public boolean isModified() {
//        System.out.println("isModified()");
        final int paintGroupCount = savedValues.getPaintGroupCount();
        if (paintGroupCount != paintGroupRows.size()) {
            return true;
        }

        for (int i = 0; i < panelColorScheme.getComponentCount() - 1; i++) {
            if (paintGroupRows.get(i).isModified(savedValues.getPaintGroupProperties(i))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void disposeUIResources() {
        paintGroupRows.clear();
        mainPanel = null;
    }


    private void setupUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(3, 3));
        panelColorScheme = new JPanel();
        final LayoutManager gridLayoutManager = new BoxLayout(panelColorScheme, BoxLayout.PAGE_AXIS);
        panelColorScheme.setLayout(gridLayoutManager);
        mainPanel.add(panelColorScheme, BorderLayout.CENTER);
        panelColorScheme.setBorder(BorderFactory.createTitledBorder("Color Scheme"));


        buttonAddSelectionGroup = new JButton();
        buttonAddSelectionGroup.setText("Add New Paint Group");
        panelColorScheme.add(buttonAddSelectionGroup);
        buttonAddSelectionGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StickySelectionPreferences.this.addNewPaintGroupRow(null);
                panelColorScheme.updateUI();
            }
        });

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
        if(mainPanel == null) {
            return;
        }
//        System.out.println("apply()");

        savedValues.removeFrom(paintGroupRows.size());
        for (int i = 0; i < paintGroupRows.size(); i++) {
            if (savedValues.getPaintGroupCount() <= i) {
                savedValues.addNewPaintGroup();
            }
            paintGroupRows.get(i).getData(savedValues.getPaintGroupProperties(i));
        }
        panelColorScheme.updateUI();

        StickySelectionAppComponent applicationComponent = ApplicationManager
                .getApplication()
                .getComponent(StickySelectionAppComponent.class);
        applicationComponent.updateAllHighlighters();
    }

    @Override
    public void reset() {
        if (mainPanel == null) {
            return;
        }
//        System.out.println("reset()");
        while (paintGroupRows.size() > savedValues.getPaintGroupCount()) {
            panelColorScheme.remove(paintGroupRows.get(paintGroupRows.size() - 1).$$$getRootComponent$$$());
            paintGroupRows.remove(paintGroupRows.size() - 1);
        }
        for (int i = 0; i < savedValues.getPaintGroupCount(); i++) {
            if (paintGroupRows.size() <= i) {
                addNewPaintGroupRow(savedValues.getPaintGroupProperties(i));
            } else {
                paintGroupRows.get(i).setData(savedValues.getPaintGroupProperties(i));
            }
        }
        panelColorScheme.updateUI();
    }


}

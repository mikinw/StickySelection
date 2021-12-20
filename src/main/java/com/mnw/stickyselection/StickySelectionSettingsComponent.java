package com.mnw.stickyselection;

import com.mnw.stickyselection.infrastructure.RandomPaintGroupData;
import com.mnw.stickyselection.model.PaintGroupDataBean;
import com.mnw.stickyselection.model.ValuesRepository;
import com.mnw.stickyselection.preferences.PaintGroupRow;
import com.mnw.stickyselection.preferences.SettingsForm;
import com.mnw.stickyselection.preferences.StickySelectionPreferences;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StickySelectionSettingsComponent {

    private JComponent mainPanel;
    private JCheckBox checkboxCycleThrough;
    private JLabel refreshWarning;
    private JCheckBox checkboxPersistHighlights;

    private JButton buttonAddSelectionGroup;

    private JPanel panelColorScheme;

    private List<Integer> deletedDataBeans = new ArrayList<>();
    private java.util.List<PaintGroupRow> paintGroupRows = new ArrayList<>();



    public StickySelectionSettingsComponent(final ValuesRepository savedValues) {
        setupUI();
        setData(savedValues);

    }

    public JComponent getPreferredFocusedComponent() {
        return null;
    }

    public JComponent getPanel() {
        return mainPanel;
    }


    private void setupUI() {
        final SettingsForm settingsForm = new SettingsForm();
        mainPanel = settingsForm.getRootComponent();

        panelColorScheme = settingsForm.getPanelColorScheme();
        panelColorScheme.setLayout(new BoxLayout(panelColorScheme, BoxLayout.Y_AXIS));
        checkboxCycleThrough = settingsForm.getCheckboxCycleThrough();
        buttonAddSelectionGroup = settingsForm.getButtonAddSelectionGroup();
        refreshWarning = settingsForm.getRefreshWarning();
        checkboxPersistHighlights = settingsForm.getCheckBoxPersistHighlights();

        buttonAddSelectionGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StickySelectionSettingsComponent.this.addNewPaintGroupRow(RandomPaintGroupData.createBean());
                panelColorScheme.updateUI();
            }
        });

    }

    public void setData(ValuesRepository savedValues) {
        checkboxCycleThrough.setSelected(savedValues.getIsCycleThroughEnabled());
        checkboxPersistHighlights.setSelected(savedValues.getPersistHighlights());


        panelColorScheme.removeAll();
        paintGroupRows.clear();

        for (int i = 0; i < savedValues.getPaintGroupCount(); i++) {
            final PaintGroupDataBean paintGroupProperties = savedValues.getPaintGroupProperties(i);
            addNewPaintGroupRow(paintGroupProperties);

            //setDataForSelectionGroupFrom(savedValues, i);
        }
    }


    private void addNewPaintGroupRow(@NotNull final PaintGroupDataBean paintGroupProperties) {
        final PaintGroupRow paintGroupRow = new PaintGroupRow();
        paintGroupRow.setData(paintGroupProperties);
        paintGroupRow.addRemoveClickListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String actionCommand = e.getActionCommand();
                if (actionCommand.equals("RemovePaintGroup")) {
                    panelColorScheme.remove(paintGroupRow.getRootComponent());
                    panelColorScheme.updateUI();
                }
                paintGroupRows.remove(paintGroupRow);
            }
        });
        panelColorScheme.add(paintGroupRow.getRootComponent());
        paintGroupRows.add(paintGroupRow);
    }

    public boolean isCycleThrough() {
        return checkboxCycleThrough.isSelected();
    }

    public boolean isPersistHighlights() {
        return checkboxPersistHighlights.isSelected();
    }

    public List<PaintGroupDataBean> getPaintGroups() {
        return paintGroupRows.stream().map(PaintGroupRow::getData).collect(Collectors.toList());
    }
}

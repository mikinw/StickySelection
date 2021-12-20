package com.mnw.stickyselection.preferences;

import javax.swing.*;

public class SettingsForm {

    private JCheckBox checkboxCycleThrough;
    private JButton buttonAddSelectionGroup;
    private JLabel refreshWarning;
    private JPanel panelColorScheme;
    private JPanel mainPanel;
    private JCheckBox checkBoxPersistHighlights;

    public JCheckBox getCheckboxCycleThrough() {
        return checkboxCycleThrough;
    }

    public JButton getButtonAddSelectionGroup() {
        return buttonAddSelectionGroup;
    }

    public JLabel getRefreshWarning() {
        return refreshWarning;
    }

    public JPanel getPanelColorScheme() {
        return panelColorScheme;
    }

    public JCheckBox getCheckBoxPersistHighlights() {
        return checkBoxPersistHighlights;
    }


    public JPanel getRootComponent() {
        return mainPanel;
    }


}

package com.mnw.stickyselection.preferences;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TODO description of this class is missing
 */
public class PreferencesSetColorActionListener implements ActionListener {

    private final String dialogTitle;

    public PreferencesSetColorActionListener(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        final Object source = actionEvent.getSource();
        if (!(source instanceof ColorButton)) {
            return;
        }
        final ColorButton colorButton = (ColorButton) source;
        Color currentColor = colorButton.getColor();
        //noinspection UseJBColor
        colorButton.setColor(new Color(
                currentColor.getRed(),
                currentColor.getGreen(),
                currentColor.getBlue(),
                250));
        Color newColor = JColorChooser.showDialog(colorButton, dialogTitle, currentColor);
        if (newColor == null) {
            colorButton.setColor(currentColor);
            return;
        }
        colorButton.setColor(newColor);
    }

}

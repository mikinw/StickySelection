package com.mnw.stickyselection.preferences;

import javax.swing.*;
import java.awt.*;

/**
 * TODO description of this class is missing
 */
public class ColorButton extends JButton {

    public static final int PREFERRED_WIDTH = 45;
    public static final int PREFERRED_HEIGHT = 17;

    public void setColor(Color color) {
        final ColorImage colorImage = new ColorImage(PREFERRED_WIDTH, PREFERRED_HEIGHT, color);
        final ImageIcon imageIcon = new ImageIcon(colorImage);
        super.setIcon(imageIcon);
    }

    public Color getColor() {
        ImageIcon imageIcon = (ImageIcon) super.getIcon();
        ColorImage colorImage = (ColorImage)imageIcon.getImage();
        return colorImage.getColor();
    }
}

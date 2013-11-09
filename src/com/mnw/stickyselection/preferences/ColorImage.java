package com.mnw.stickyselection.preferences;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * TODO description of this class is missing
 */
public class ColorImage extends BufferedImage {
    protected Color color = null;

    public ColorImage(int width, int height, Color color) {
        super(width, height, TYPE_INT_RGB);
        this.color = color;
        Graphics2D g2d = createGraphics();
        g2d.setPaint(this.color);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    public Color getColor() {
        return(color);
    }
}



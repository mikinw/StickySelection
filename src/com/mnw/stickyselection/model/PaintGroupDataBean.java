package com.mnw.stickyselection.model;

import com.intellij.util.xmlb.annotations.Transient;

import java.awt.*;

public class PaintGroupDataBean {
    private String shortcut;
    private boolean frameNeeded;
    private int layer;
    private boolean markerNeeded;
    private Color color;

    public PaintGroupDataBean() {}

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(final String shortcut) {
        this.shortcut = shortcut;
    }

    public boolean isFrameNeeded() {
        return frameNeeded;
    }

    public void setFrameNeeded(final boolean frameNeeded) {
        this.frameNeeded = frameNeeded;
    }

    public int getHughlightLayer() {
        return layer;
    }

    public void setLayer(final int layer) {
        this.layer = layer;
    }

    public boolean isMarkerNeeded() {
        return markerNeeded;
    }

    public void setMarkerNeeded(final boolean markerNeeded) {
        this.markerNeeded = markerNeeded;
    }

    @Transient
    public Color getColor() {
        return color;
    }

    @Transient
    public void setColor(Color color) {
        this.color = color;
    }


    public String getColorString() {
        return createColorString(color != null ? color : Color.cyan);
    }

    private static String createColorString(Color color) {
        return String.format("%d,%d,%d", color.getRed(), color.getGreen(), color.getBlue());
    }


    public void setColorString(String color) {
        this.color = decodeColorString(color);
    }

    @SuppressWarnings("UseJBColor")
    private static Color decodeColorString(String color) {
        final String[] split = color.split(",");
        return new Color(
                Integer.decode(split[0]),
                Integer.decode(split[1]),
                Integer.decode(split[2])
                );
    }
}

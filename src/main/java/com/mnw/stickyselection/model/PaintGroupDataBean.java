package com.mnw.stickyselection.model;

import com.intellij.ui.JBColor;
import com.intellij.util.xmlb.annotations.Transient;

import java.awt.*;

public class PaintGroupDataBean {
    private String shortcut;
    private boolean frameNeeded;
    private int layer = 4200;
    private boolean markerNeeded;
    private Color color;
    private int id;

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

    public int getHighlightLayer() {
        return layer;
    }

    public void setHighlightLayer(final int layer) {
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
        return createColorString(color != null ? color : JBColor.CYAN);
    }

    private static String createColorString(Color color) {
        return String.format("%d,%d,%d,%d", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }


    public void setColorString(String color) {
        this.color = decodeColorString(color);
    }

    @SuppressWarnings("UseJBColor")
    private static Color decodeColorString(String color) {
        final String[] split = color.split(",");
        if (split.length >= 4) {
            return new Color(
                    Integer.decode(split[0]),
                    Integer.decode(split[1]),
                    Integer.decode(split[2]),
                    Integer.decode(split[3])
            );
        }
        if (split.length == 3) {

            return new Color(
                    Integer.decode(split[0]),
                    Integer.decode(split[1]),
                    Integer.decode(split[2])
            );
        }
        return Color.RED;
    }

    @Override
    @Transient
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaintGroupDataBean that = (PaintGroupDataBean) o;

        if (frameNeeded != that.frameNeeded) return false;
        if (layer != that.layer) return false;
        if (markerNeeded != that.markerNeeded) return false;
        if (shortcut != null ? !shortcut.equals(that.shortcut) : that.shortcut != null) return false;
        return color.equals(that.color);

    }

    @Override
    @Transient
    public int hashCode() {
        int result = shortcut != null ? shortcut.hashCode() : 0;
        result = 31 * result + (frameNeeded ? 1 : 0);
        result = 31 * result + layer;
        result = 31 * result + (markerNeeded ? 1 : 0);
        result = 31 * result + color.hashCode();
        return result;
    }

    @Transient
    /*package*/ void setId(int id) {
        assert this.id == 0;
        this.id = id;
    }

    @Transient
    public int getId() {
        return id;
    }

    @Transient
    public PaintGroupDataBean copy() {
        final PaintGroupDataBean ret = new PaintGroupDataBean();
        ret.color = color;
        ret.frameNeeded = frameNeeded;
        ret.layer = layer;
        ret.markerNeeded = markerNeeded;
        ret.shortcut = shortcut;
        return ret;
    }
}

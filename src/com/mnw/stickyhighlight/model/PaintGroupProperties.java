package com.mnw.stickyhighlight.model;

import java.awt.*;

/**
 * TODO description of this class is missing
 */
public class PaintGroupProperties implements PaintGroupPropertiesGetter {
    private final int highlightLayerOfSelectionGroup;
    private final boolean markerEnabledForSelectionGroup;
    private final Color colorOfSelectionGroup;

    public PaintGroupProperties(int highlightLayerOfSelectionGroup, boolean markerEnabledForSelectionGroup, Color colorOfSelectionGroup) {

        this.highlightLayerOfSelectionGroup = highlightLayerOfSelectionGroup;
        this.markerEnabledForSelectionGroup = markerEnabledForSelectionGroup;
        this.colorOfSelectionGroup = colorOfSelectionGroup;
    }

    @Override
    public Color getColor() {
        return colorOfSelectionGroup;
    }

    @Override
    public int getHighlightLayer() {
        return highlightLayerOfSelectionGroup;
    }

    @Override
    public boolean isMarkerEnabled() {
        return markerEnabledForSelectionGroup;
    }
}

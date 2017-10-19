package com.mnw.stickyselection.model;


import java.awt.Color;

public class PaintGroupPropertiesImpl implements PaintGroupProperties {
    private final int highlightLayerOfSelectionGroup;
    private final boolean markerEnabledForSelectionGroup;
    private final Color colorOfSelectionGroup;

    public PaintGroupPropertiesImpl(int highlightLayerOfSelectionGroup, boolean markerEnabledForSelectionGroup, Color colorOfSelectionGroup) {

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

    @Override
    public void setColor(Color color) {

    }

    @Override
    public void setHighlightLayer(int highlightLayer) {

    }

    @Override
    public void setIsMarkerEnabled(boolean isMarkerEnabled) {

    }
}

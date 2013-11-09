package com.mnw.stickyhighlight.model;

import java.awt.*;

/**
 * TODO description of this class is missing
 */
public class ValuesRepository implements ValuesRepositoryReader {

    private final int NUMBER_OF_SELECTION_GROUPS = 8;

    private boolean isPluginEnabled;
    private boolean[] markerEnabled;
    private int[] highlightLayer;
    private Color[] color;

    public ValuesRepository() {
        markerEnabled = new boolean[NUMBER_OF_SELECTION_GROUPS];
        highlightLayer = new int[NUMBER_OF_SELECTION_GROUPS];
        color = new Color[NUMBER_OF_SELECTION_GROUPS];
    }

    public void setColorOfSelectionGroup(int groupNumber, Color color) {
        this.color[groupNumber] = color;
    }

    public void setHighlightLayerOfSelectionGroup(int groupNumber, int highlightLayer) {
        this.highlightLayer[groupNumber] = highlightLayer;
    }

    public void setIsMarkerEnabledForSelectionGroup(int groupNumber, boolean markerEnabled) {
        this.markerEnabled[groupNumber] = markerEnabled;
    }

    public void setIsPluginEnabled(boolean enabled) {
        isPluginEnabled = enabled;
    }

    @Override
    public Color getColorOfSelectionGroup(int groupNumber) {
        return color[groupNumber];
    }

    @Override
    public int getHighlightLayerOfSelectionGroup(int groupNumber) {
        return highlightLayer[groupNumber];
    }

    @Override
    public boolean isMarkerEnabledForSelectionGroup(int groupNumber) {
        return markerEnabled[groupNumber];
    }

    @Override
    public boolean isPluginEnabled() {
        return isPluginEnabled;
    }
}

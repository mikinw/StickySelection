package com.mnw.stickyselection.model;

import java.awt.*;

/**
 * TODO description of this class is missing
 */
public interface PaintGroupProperties {
    Color getColor();
    int getHighlightLayer();
    boolean isMarkerEnabled();
    void setColor(Color color);
    void setHighlightLayer(int highlightLayer);
    void setIsMarkerEnabled(boolean isMarkerEnabled);
}

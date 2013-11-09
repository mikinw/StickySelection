package com.mnw.stickyhighlight.model;

import java.awt.*;

/**
 * TODO description of this class is missing
 */
public interface ValuesRepositoryReader {

    public Color getColorOfSelectionGroup(int groupNumber);

    public int getHighlightLayerOfSelectionGroup(int groupNumber);

    public boolean isMarkerEnabledForSelectionGroup(int groupNumber);

    public boolean isPluginEnabled();
}

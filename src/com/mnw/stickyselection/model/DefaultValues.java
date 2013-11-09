package com.mnw.stickyselection.model;

import java.awt.*;

/**
 * TODO description of this class is missing
 */
public class DefaultValues implements ValuesRepositoryReader {

    public static final String[] DEFAULT_COLORS = {
        "0xB060D0",
        "0xFFFF33",
        "0x33FFFF",
        "0xFF33FF",
        "0x12987D",
        "0xFFA0A0",
        "0xA0FFA0",
        "0xA0A0FF",
    };
    //DEFAULT_CLASS_ACTIVE_HIGHLIGHT_COLOR = "0,175,175";
    public static final String DEFAULT_CLASS_HIGHLIGHT_COLOR = "128,255,255";
    public static final String DEFAULT_METHOD_ACTIVE_HIGHLIGHT_COLOR = "0,175,175";
    public static final String DEFAULT_METHOD_HIGHLIGHT_COLOR = "128,255,255";
    public static final String DEFAULT_FIELD_READ_ACTIVE_HIGHLIGHT_COLOR = "0,175,175";
    public static final String DEFAULT_FIELD_READ_HIGHLIGHT_COLOR = "128,255,255";
    public static final String DEFAULT_PARAM_READ_ACTIVE_HIGHLIGHT_COLOR = "0,175,175";
    public static final String DEFAULT_PARAM_READ_HIGHLIGHT_COLOR = "128,255,255";
    public static final String DEFAULT_LOCAL_READ_ACTIVE_HIGHLIGHT_COLOR = "0,175,175";
    public static final String DEFAULT_LOCAL_READ_HIGHLIGHT_COLOR = "128,255,255";
    public static final String DEFAULT_FIELD_WRITE_ACTIVE_HIGHLIGHT_COLOR = "175,0,0";
    public static final String DEFAULT_FIELD_WRITE_HIGHLIGHT_COLOR = "255,128,128";
    public static final String DEFAULT_PARAM_WRITE_ACTIVE_HIGHLIGHT_COLOR = "175,0,0";
    public static final String DEFAULT_PARAM_WRITE_HIGHLIGHT_COLOR = "255,128,128";
    public static final String DEFAULT_LOCAL_WRITE_ACTIVE_HIGHLIGHT_COLOR = "175,0,0";
    public static final String DEFAULT_LOCAL_WRITE_HIGHLIGHT_COLOR = "255,128,128";
    public static final String DEFAULT_OTHER_ACTIVE_HIGHLIGHT_COLOR = "0,175,175";
    public static final String DEFAULT_OTHER_HIGHLIGHT_COLOR = "128,255,255";

    public Color getColorOfSelectionGroup(int groupNumber) {
        return Color.decode(DEFAULT_COLORS[groupNumber]);
    }

    public int getHighlightLayerOfSelectionGroup(int groupNumber) {
        if (groupNumber <= 4) {
            return 4200;
        }
        else {
            return 2500;
        }
    }

    public boolean isMarkerEnabledForSelectionGroup(int groupNumber) {
        return true;
    }

    public boolean isPluginEnabled() {
        return true;
    }
}

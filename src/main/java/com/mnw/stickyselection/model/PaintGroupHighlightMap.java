package com.mnw.stickyselection.model;

import java.util.HashMap;

public class PaintGroupHighlightMap extends HashMap<Integer, EditorHighlightsForPaintGroup> {

    public HashMap<Integer, EditorHighlightsForPaintGroup> getPaintGroups() {
        return this;
    }

    /**
     * we need to have a setter, or else the xml deserialisation won't work.
     * @param paintGroups
     */
    public void setPaintGroups(HashMap<Integer, EditorHighlightsForPaintGroup> paintGroups) {
        if (paintGroups == this) {
            return;
        }
        this.clear();
        this.putAll(paintGroups);
    }
}

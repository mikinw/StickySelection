package com.mnw.stickyselection.model;

import java.util.ArrayList;
import java.util.List;

/**
 * we need to have this facade, or else the xml deserialisation won't work.
 */
public class EditorHighlightsForPaintGroup extends ArrayList<HighlightOffset> {

    public List<HighlightOffset> getHighlights() {
        return this;
    }

    /**
     * we need to have a setter, or else the xml deserialisation won't work.
     * @param highlights
     */
    public void setHighlights(List<HighlightOffset> highlights) {
        if (highlights == this) {
            return;
        }
        this.clear();
        this.addAll(highlights);
    }

    @Override
    public EditorHighlightsForPaintGroup clone() {
        return (EditorHighlightsForPaintGroup) super.clone();
    }
}

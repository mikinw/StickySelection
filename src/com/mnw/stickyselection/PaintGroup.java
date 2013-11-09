package com.mnw.stickyselection;

import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;

import java.awt.*;
import java.util.ArrayList;

/**
 * TODO description of this class is missing
 */
public class PaintGroup {

    protected final ArrayList<RangeHighlighter> highlighters;

    public PaintGroup() {
        highlighters = new ArrayList<RangeHighlighter>();
    }

    public void add(RangeHighlighter rangeHighlighter) {
        highlighters.add(rangeHighlighter);
    }

    public void clear(MarkupModel markupModel) {
        for(RangeHighlighter highlight : highlighters) {
            markupModel.removeHighlighter(highlight);
        }
        highlighters.clear();
    }

    public void repaint(Color color) {
        for (RangeHighlighter highlighter : highlighters) {
            highlighter.getTextAttributes().setBackgroundColor(color);
        }
    }
}

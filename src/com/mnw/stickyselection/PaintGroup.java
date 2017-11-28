package com.mnw.stickyselection;

import com.intellij.openapi.editor.ex.MarkupModelEx;
import com.intellij.openapi.editor.ex.RangeHighlighterEx;
import com.intellij.openapi.editor.markup.*;
import com.intellij.util.Consumer;
import com.mnw.stickyselection.model.PaintGroupDataBean;

import java.util.ArrayList;
import java.util.List;

public class PaintGroup {

    protected final ArrayList<RangeHighlighter> highlighters;
    private PaintGroupDataBean paintGroupProperties;

    public PaintGroup(PaintGroupDataBean paintGroupProperties) {
        highlighters = new ArrayList<>();
        this.paintGroupProperties = paintGroupProperties;
    }

    public void add(RangeHighlighter rangeHighlighter) {

        highlighters.add(rangeHighlighter);
    }

    public void clear(MarkupModel markupModel) {
        final ArrayList<RangeHighlighter> copy = (ArrayList<RangeHighlighter>) highlighters.clone();
        for(RangeHighlighter highlight : copy) {
            markupModel.removeHighlighter(highlight);
        }
        highlighters.clear();
    }

/*    public void repaint(Color color) {
        for (RangeHighlighter highlighter : highlighters) {
            final TextAttributes textAttributes = highlighter.getTextAttributes();
            textAttributes.setBackgroundColor(color);
        }
    }*/

    public boolean has(RangeHighlighter rangeHighlighter) {
        //for (RangeHighlighter highlighter : highlighters) {
        //    if (highlighter.getStartOffset() == rangeHighlighter.getStartOffset() && highlighter.getEndOffset() == rangeHighlighter.getEndOffset() && highlighter.get)
        //}
        return false;
    }

    public boolean hasSameDataBean(PaintGroupDataBean paintGroupProperties) {
        return paintGroupProperties == this.paintGroupProperties;
    }

    public void repaint(PaintGroupDataBean paintGroupProperties, MarkupModel markupModel) {
        this.paintGroupProperties = paintGroupProperties;
        if (highlighters.isEmpty()) { return; }

        final TextAttributes newTextAttributes = new TextAttributes();
        newTextAttributes.setBackgroundColor(paintGroupProperties.getColor());
        if (paintGroupProperties.isFrameNeeded()) {
            newTextAttributes.setEffectType(EffectType.BOXED);
            newTextAttributes.setEffectColor(paintGroupProperties.getColor());
        }

        if (highlighters.get(0).getLayer() == paintGroupProperties.getHighlightLayer()) {
            for (RangeHighlighter highlighter : highlighters) {
                ((MarkupModelEx) markupModel).setRangeHighlighterAttributes(highlighter, newTextAttributes);
                if (paintGroupProperties.isMarkerNeeded()) {
                    highlighter.setErrorStripeMarkColor(paintGroupProperties.getColor());
                } else {
                    highlighter.setErrorStripeMarkColor(null);
                }
            }
        } else {

            final List<RangeHighlighter> newHighLighters = new ArrayList<>(highlighters.size());
            final ArrayList<RangeHighlighter> copy = (ArrayList<RangeHighlighter>) highlighters.clone();

            for (RangeHighlighter highlighter : copy) {
                final int startOffset = highlighter.getStartOffset();
                final int endOffset = highlighter.getEndOffset();
                markupModel.removeHighlighter(highlighter);
                final RangeHighlighter rangeHighlighter = markupModel.addRangeHighlighter(
                        startOffset,
                        endOffset,
                        paintGroupProperties.getHighlightLayer(),
                        newTextAttributes,
                        HighlighterTargetArea.EXACT_RANGE);
                if (paintGroupProperties.isMarkerNeeded()) {
                    highlighter.setErrorStripeMarkColor(paintGroupProperties.getColor());
                }
                newHighLighters.add(rangeHighlighter);
            }
            highlighters.addAll(newHighLighters);
        }

    }

    public void remove(RangeHighlighter rangeHighlighter) {
        // TODO: 2017. 11. 18. this could be faster with binary search

        highlighters.remove(rangeHighlighter);

    }
}

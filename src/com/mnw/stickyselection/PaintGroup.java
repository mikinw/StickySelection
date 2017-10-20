package com.mnw.stickyselection;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.*;
import com.mnw.stickyselection.model.PaintGroupDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO description of this class is missing
 */
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
        for(RangeHighlighter highlight : highlighters) {
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
        final TextAttributes textAttributes = new TextAttributes();
        textAttributes.setBackgroundColor(paintGroupProperties.getColor());
        if (paintGroupProperties.isFrameNeeded()) {
            textAttributes.setEffectType(EffectType.BOXED);
            textAttributes.setEffectColor(paintGroupProperties.getColor());
        }
        final List<RangeHighlighter> newHighLighters = new ArrayList<>(highlighters.size());
        for (RangeHighlighter highlighter : highlighters) {
            final int startOffset = highlighter.getStartOffset();
            final int endOffset = highlighter.getEndOffset();
            markupModel.removeHighlighter(highlighter);
            final RangeHighlighter rangeHighlighter = markupModel.addRangeHighlighter(
                    startOffset,
                    endOffset,
                    paintGroupProperties.getHighlightLayer(),
                    textAttributes,
                    HighlighterTargetArea.EXACT_RANGE);
            if (paintGroupProperties.isMarkerNeeded()) {
                rangeHighlighter.setErrorStripeMarkColor(paintGroupProperties.getColor());
            }
            newHighLighters.add(rangeHighlighter);
        }
        highlighters.clear();
        highlighters.addAll(newHighLighters);

    }
}

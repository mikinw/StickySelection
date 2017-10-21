package com.mnw.stickyselection.infrastructure;

import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.mnw.stickyselection.StickySelectionEditorComponent;

import java.util.ArrayList;

public class FindClosestHighlighterPrevious implements FindClosestHighlighter {
    @Override
    public void findUpcoming(int currentCaret,
                             final StickySelectionEditorComponent.CurrentBest currentBest,
                             int paintGroupNumber, final ArrayList<RangeHighlighter> highlighters) {
        for (int i = highlighters.size() - 1; i >= 0; i--) {
            if (highlighters.get(i).getEndOffset() < currentCaret) {
                if (currentCaret - highlighters.get(i).getEndOffset() < currentBest.getClosestDistance()) {
                    currentBest.setClosestDistance(currentCaret - highlighters.get(i).getEndOffset());
                    currentBest.setCaretOffset(highlighters.get(i).getStartOffset());
                    currentBest.setPaintGroup(paintGroupNumber);
                }
            }
        }
    }

    @Override
    public void findFromStart(int currentCaret, int documentLength,
                              StickySelectionEditorComponent.CurrentBest currentBest, int paintGroupNumber,
                              ArrayList<RangeHighlighter> highlighters) {
        if (highlighters.isEmpty()) {
            return;
        }
        final RangeHighlighter lastRangeHighlighter = highlighters.get(highlighters.size() - 1);
        final int distanceFromCurrentCaret = documentLength - lastRangeHighlighter.getEndOffset() + currentCaret;
        if (distanceFromCurrentCaret < currentBest.getClosestDistance()) {
            if (!highlighters.isEmpty()) {
                currentBest.setCaretOffset(lastRangeHighlighter.getEndOffset());
                currentBest.setClosestDistance(distanceFromCurrentCaret);
                currentBest.setPaintGroup(paintGroupNumber);
            }
        }
    }
}

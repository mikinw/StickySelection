package com.mnw.stickyselection.infrastructure;

import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.mnw.stickyselection.StickySelectionEditorComponent;

import java.util.ArrayList;

public class FindClosestHighlighterNext implements FindClosestHighlighter {
    @Override
    public void findUpcoming(int currentCaret,
                             StickySelectionEditorComponent.CurrentBest currentBest,
                             int paintGroupNumber, ArrayList<RangeHighlighter> highlighters) {
        for (int i = 0; i < highlighters.size(); i++) {
            if (highlighters.get(i).getStartOffset() > currentCaret) {
                final int distanceFromCurrentCaret = highlighters.get(i).getStartOffset() - currentCaret;
                if (distanceFromCurrentCaret < currentBest.getClosestDistance()) {
                    currentBest.setClosestDistance(distanceFromCurrentCaret);
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
        final int distanceFromCurrentCaret = highlighters.get(0).getStartOffset() + (documentLength - currentCaret);
        if (distanceFromCurrentCaret < currentBest.getClosestDistance()) {
            if (!highlighters.isEmpty()) {
                currentBest.setCaretOffset(highlighters.get(0).getStartOffset());
                currentBest.setClosestDistance(distanceFromCurrentCaret);
                currentBest.setPaintGroup(paintGroupNumber);
            }
        }
    }
}

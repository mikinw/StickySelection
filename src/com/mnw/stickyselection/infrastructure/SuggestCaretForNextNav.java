package com.mnw.stickyselection.infrastructure;

import com.intellij.openapi.editor.markup.RangeHighlighter;

import java.util.ArrayList;

public class SuggestCaretForNextNav implements SuggestCaret {
    public SuggestCaretForNextNav() { }

    @Override
    public int findCaretInPaintGroup(int currentCaret, ArrayList<RangeHighlighter> highlighters,
                                     boolean isCycleThroughEnabled) {
        for (int i = 0; i < highlighters.size() - 1; i++) {
            if (caretIsInside(currentCaret, highlighters.get(i))) {
                int next = i + 1;

                if (highlighters.get(next).getStartOffset() > currentCaret) {
                    return highlighters.get(next).getStartOffset();
                }
            }
        }
        if (caretIsInside(currentCaret, highlighters.get(highlighters.size()-1))) {
            int next = isCycleThroughEnabled ? 0 : highlighters.size()-1;
            if (highlighters.get(next).getStartOffset() != currentCaret) {
                return highlighters.get(next).getStartOffset();
            }
        }

        return -1;
    }

    private static boolean caretIsInside(int currentCaret, RangeHighlighter rangeHighlighter) {
        return rangeHighlighter.getStartOffset() <= currentCaret
                && rangeHighlighter.getEndOffset() >= currentCaret;
    }
}

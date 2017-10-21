package com.mnw.stickyselection.infrastructure;

import com.intellij.openapi.editor.markup.RangeHighlighter;

import java.util.ArrayList;

public class SuggestCaretForPrevious implements SuggestCaret {
    public SuggestCaretForPrevious() { }

    @Override
    public int findCaretInPaintGroup(int currentCaret, ArrayList<RangeHighlighter> highlighters,
                                     boolean isCycleThroughEnabled) {
        for (int i = highlighters.size() - 1; i >= 0; i--) {
            if (highlighters.get(i).getStartOffset() <= currentCaret
                    && highlighters.get(i).getEndOffset() >= currentCaret) {
                int next;
                if (i == 0) {
                    next = isCycleThroughEnabled ? highlighters.size() - 1 : 0;
                } else {
                    next = i - 1;
                }
                return highlighters.get(next).getStartOffset();
            }
        }
        return -1;
    }
}

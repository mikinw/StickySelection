package com.mnw.stickyselection.infrastructure;

import com.intellij.openapi.editor.markup.RangeHighlighter;

import java.util.ArrayList;

public class SuggestCaretForNext implements SuggestCaret {
    public SuggestCaretForNext() { }

    @Override
    public int findCaretInPaintGroup(int currentCaret, ArrayList<RangeHighlighter> highlighters,
                                     boolean isCycleThroughEnabled) {
        for (int i = 0; i < highlighters.size(); i++) {
            if (highlighters.get(i).getStartOffset() <= currentCaret
                    && highlighters.get(i).getEndOffset() >= currentCaret) {
                int next;
                if (i == highlighters.size() - 1) {
                    next = isCycleThroughEnabled ? 0 : i;
                } else {
                    next = i + 1;
                }

                return highlighters.get(next).getStartOffset();
            }
        }
        return -1;
    }
}

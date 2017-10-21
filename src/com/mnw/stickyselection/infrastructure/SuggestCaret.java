package com.mnw.stickyselection.infrastructure;

import com.intellij.openapi.editor.markup.RangeHighlighter;

import java.util.ArrayList;

public interface SuggestCaret {
    int findCaretInPaintGroup(int currentCaret, ArrayList<RangeHighlighter> highlighters, boolean isCycleThroughEnabled);
}

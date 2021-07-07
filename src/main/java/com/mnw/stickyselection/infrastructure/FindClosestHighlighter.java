package com.mnw.stickyselection.infrastructure;

import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.mnw.stickyselection.StickySelectionEditorComponent;

import java.util.ArrayList;

public interface FindClosestHighlighter {
    void findUpcoming(int currentCaret, StickySelectionEditorComponent.CurrentBest currentBest,
                      int paintGroupNumber, ArrayList<RangeHighlighter> highlighters);
    void findFromStart(int currentCaret, int documentLength, StickySelectionEditorComponent.CurrentBest currentBest,
                       int paintGroupNumber, ArrayList<RangeHighlighter> highlighters);
}

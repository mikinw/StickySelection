package com.mnw.stickyselection.actions;

import com.mnw.stickyselection.infrastructure.FindClosestHighlighterNext;

public class NavigateToAnyNextPaint extends StickyEditorAction {
    @Override
    protected void actionImpl() {
        stickySelectionEditorComponent.navigateToClosestPaint(new FindClosestHighlighterNext());
    }
}

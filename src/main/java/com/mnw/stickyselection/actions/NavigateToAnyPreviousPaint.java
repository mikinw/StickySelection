package com.mnw.stickyselection.actions;

import com.mnw.stickyselection.infrastructure.FindClosestHighlighterPrevious;

public class NavigateToAnyPreviousPaint extends StickyEditorAction {
    @Override
    protected void actionImpl() {
        stickySelectionEditorComponent.navigateToClosestPaint(new FindClosestHighlighterPrevious());
    }
}

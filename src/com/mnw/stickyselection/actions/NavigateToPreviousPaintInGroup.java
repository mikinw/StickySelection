package com.mnw.stickyselection.actions;

import com.mnw.stickyselection.infrastructure.FindClosestHighlighterPrevious;
import com.mnw.stickyselection.infrastructure.SuggestCaretForPrevious;

public class NavigateToPreviousPaintInGroup extends StickyEditorAction {
    @Override
    protected void actionImpl() {
        stickySelectionEditorComponent.navigateToPaint(
                new SuggestCaretForPrevious(),
                new FindClosestHighlighterPrevious()
        );
    }
}

package com.mnw.stickyselection.actions;

import com.mnw.stickyselection.infrastructure.FindClosestHighlighterPrevious;
import com.mnw.stickyselection.infrastructure.SuggestCaretForPreviousNav;

public class NavigateToPreviousPaintInGroup extends StickyEditorAction {
    @Override
    protected void actionImpl() {
        stickySelectionEditorComponent.navigateToPaint(
                new SuggestCaretForPreviousNav(),
                new FindClosestHighlighterPrevious()
        );
    }
}

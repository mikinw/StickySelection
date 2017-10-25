package com.mnw.stickyselection.actions;

import com.mnw.stickyselection.infrastructure.FindClosestHighlighterPrevious;
import com.mnw.stickyselection.infrastructure.SuggestCaretForPreviousMav;

public class NavigateToPreviousPaintInGroup extends StickyEditorAction {
    @Override
    protected void actionImpl() {
        stickySelectionEditorComponent.navigateToPaint(
                new SuggestCaretForPreviousMav(),
                new FindClosestHighlighterPrevious()
        );
    }
}

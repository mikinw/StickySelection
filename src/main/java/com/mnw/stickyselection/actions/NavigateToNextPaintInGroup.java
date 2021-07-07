package com.mnw.stickyselection.actions;

import com.mnw.stickyselection.infrastructure.FindClosestHighlighterNext;
import com.mnw.stickyselection.infrastructure.SuggestCaretForNextNav;

public class NavigateToNextPaintInGroup extends StickyEditorAction {
    @Override
    protected void actionImpl() {
        stickySelectionEditorComponent.navigateToPaint(
                new SuggestCaretForNextNav(),
                new FindClosestHighlighterNext()
        );
    }
}

package com.mnw.stickyselection.actions;

public class NavigateToNextPaint extends StickyEditorAction {
    @Override
    protected void actionImpl() {
        stickySelectionEditorComponent.navigateToNextPaint();
    }
}

package com.mnw.stickyselection.actions;

public class ClearAllPaintGroupAction extends StickyEditorAction {
    @Override
    protected void actionImpl() {
        stickySelectionEditorComponent.clearAll();
    }
}

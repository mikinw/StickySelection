package com.mnw.stickyselection.actions;

public class UndoLastPaintAction extends StickyEditorAction {

    @Override
    protected void actionImpl() {
        stickySelectionEditorComponent.undoLastPaint();
    }
}

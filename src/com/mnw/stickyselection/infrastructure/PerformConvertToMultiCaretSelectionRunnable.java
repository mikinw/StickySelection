package com.mnw.stickyselection.infrastructure;

import com.mnw.stickyselection.StickySelectionEditorComponent;

public class PerformConvertToMultiCaretSelectionRunnable implements PerformStickyActionRunnable {
    private StickySelectionEditorComponent editorComponent;

    public PerformConvertToMultiCaretSelectionRunnable(StickySelectionEditorComponent editorComponent) {
        this.editorComponent = editorComponent;
    }

    @Override
    public void run(int paintGroup) {
        editorComponent.convertPaintGroupToSelection(paintGroup);
    }
}

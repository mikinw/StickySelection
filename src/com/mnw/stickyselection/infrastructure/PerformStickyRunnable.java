package com.mnw.stickyselection.infrastructure;

import com.mnw.stickyselection.StickySelectionEditorComponent;

public class PerformStickyRunnable implements PerformStickyActionRunnable {
    private StickySelectionEditorComponent editorComponent;

    public PerformStickyRunnable(StickySelectionEditorComponent editorComponent) {
        this.editorComponent = editorComponent;
    }

    @Override
    public void run(int paintGroup) {
        editorComponent.paintSelection(paintGroup);
    }

}

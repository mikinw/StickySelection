package com.mnw.stickyselection.infrastructure;

import com.mnw.stickyselection.StickySelectionEditorComponent;

public class PerformClearRunnable implements PerformStickyActionRunnable {
    private StickySelectionEditorComponent stickySelectionEditorComponent;

    public PerformClearRunnable(StickySelectionEditorComponent stickyEditorComponent) {this
            .stickySelectionEditorComponent = stickyEditorComponent;}

    @Override
    public void run(int paintGroup) {
        stickySelectionEditorComponent.clearPaintGroup(paintGroup);
    }
}

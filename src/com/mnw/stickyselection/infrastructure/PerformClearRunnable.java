package com.mnw.stickyselection.infrastructure;

import com.mnw.stickyselection.StickySelectionEditorComponent;

public class PerformClearRunnable implements Runnable {
    private final StickySelectionEditorComponent stickySelectionEditorComponent;
    private final int paintGroup;

    private PerformClearRunnable(final StickySelectionEditorComponent stickyEditorComponent, int paintGroup) {
        this.stickySelectionEditorComponent = stickyEditorComponent;
        this.paintGroup = paintGroup;
    }

    @Override
    public void run() {
        stickySelectionEditorComponent.clearPaintGroup(paintGroup);
    }

    public static PerformRunnableFactory getFactory(StickySelectionEditorComponent stickySelectionEditorComponent) {
        return new PerformClearRunnableFactory(stickySelectionEditorComponent);
    }

    private static class PerformClearRunnableFactory implements PerformRunnableFactory {
        private final StickySelectionEditorComponent stickySelectionEditorComponent;

        public PerformClearRunnableFactory(final StickySelectionEditorComponent stickySelectionEditorComponent) {
            this.stickySelectionEditorComponent = stickySelectionEditorComponent;
        }

        @Override
        public Runnable createPerformAction(int groupNumber) {
            return new PerformClearRunnable(stickySelectionEditorComponent, groupNumber);
        }
    }
}

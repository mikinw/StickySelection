package com.mnw.stickyselection.infrastructure;

import com.mnw.stickyselection.StickySelectionEditorComponent;

public class PerformClearUniqueRunnable implements Runnable {
    private final StickySelectionEditorComponent editorComponent;
    private final int paintGroup;

    private PerformClearUniqueRunnable(StickySelectionEditorComponent editorComponent, int paintGroup) {
        this.editorComponent = editorComponent;
        this.paintGroup = paintGroup;
    }

    @Override
    public void run() {
        editorComponent.clearAtCaret(paintGroup);
    }

    public static PerformRunnableFactory getFactory(StickySelectionEditorComponent stickySelectionEditorComponent) {
        return new PerformClearUniqueRunnable.PerformStickyPaintRunnableFactory(stickySelectionEditorComponent);
    }


    private static class PerformStickyPaintRunnableFactory implements PerformRunnableFactory {
        private final StickySelectionEditorComponent stickySelectionEditorComponent;

        public PerformStickyPaintRunnableFactory(final StickySelectionEditorComponent stickySelectionEditorComponent) {
            this.stickySelectionEditorComponent = stickySelectionEditorComponent;
        }

        @Override
        public Runnable createPerformAction(int groupNumber) {
            return new PerformClearUniqueRunnable(stickySelectionEditorComponent, groupNumber);
        }
    }
}

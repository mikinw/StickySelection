package com.mnw.stickyselection.infrastructure;

import com.mnw.stickyselection.StickySelectionEditorComponent;

public class PerformStickyPaintRunnable implements Runnable {
    private final StickySelectionEditorComponent editorComponent;
    private final int paintGroup;

    private PerformStickyPaintRunnable(StickySelectionEditorComponent editorComponent, int paintGroup) {
        this.editorComponent = editorComponent;
        this.paintGroup = paintGroup;
    }

    @Override
    public void run() {
        editorComponent.paintSelection(paintGroup);
    }

    public static PerformRunnableFactory getFactory(StickySelectionEditorComponent stickySelectionEditorComponent) {
        return new PerformStickyPaintRunnableFactory(stickySelectionEditorComponent);
    }


    private static class PerformStickyPaintRunnableFactory implements PerformRunnableFactory {
        private final StickySelectionEditorComponent stickySelectionEditorComponent;

        public PerformStickyPaintRunnableFactory(final StickySelectionEditorComponent stickySelectionEditorComponent) {
            this.stickySelectionEditorComponent = stickySelectionEditorComponent;
        }

        @Override
        public Runnable createPerformAction(int paintGroup) {
            return new PerformStickyPaintRunnable(stickySelectionEditorComponent, paintGroup);
        }
    }

}

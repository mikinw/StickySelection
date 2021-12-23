package com.mnw.stickyselection.infrastructure;

import com.mnw.stickyselection.StickySelectionEditorComponent;

public class PerformStickyPaintRunnable implements Runnable {
    private final StickySelectionEditorComponent editorComponent;
    private final int paintGroup;
    private final boolean paintUnique;

    private PerformStickyPaintRunnable(StickySelectionEditorComponent editorComponent, int paintGroup, final boolean paintUnique) {
        this.editorComponent = editorComponent;
        this.paintGroup = paintGroup;
        this.paintUnique = paintUnique;
    }

    @Override
    public void run() {
        editorComponent.paintSelection(paintGroup, paintUnique);
    }

    public static PerformRunnableFactory getFactory(StickySelectionEditorComponent stickySelectionEditorComponent, final boolean paintUnique) {
        return new PerformStickyPaintRunnableFactory(stickySelectionEditorComponent, paintUnique);
    }


    private static class PerformStickyPaintRunnableFactory implements PerformRunnableFactory {
        private final StickySelectionEditorComponent stickySelectionEditorComponent;
        private final boolean paintUnique;

        public PerformStickyPaintRunnableFactory(final StickySelectionEditorComponent stickySelectionEditorComponent, final boolean paintUnique) {
            this.stickySelectionEditorComponent = stickySelectionEditorComponent;
            this.paintUnique = paintUnique;
        }

        @Override
        public Runnable createPerformAction(int paintGroup) {
            return new PerformStickyPaintRunnable(stickySelectionEditorComponent, paintGroup, paintUnique);
        }
    }

}

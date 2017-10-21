package com.mnw.stickyselection.infrastructure;

import com.mnw.stickyselection.StickySelectionEditorComponent;

public class PerformConvertToMultiCaretSelectionRunnable implements Runnable {
    private final StickySelectionEditorComponent editorComponent;
    private final int paintGroupNumber;

    private PerformConvertToMultiCaretSelectionRunnable(StickySelectionEditorComponent editorComponent, int paintGroupNumber) {
        this.editorComponent = editorComponent;
        this.paintGroupNumber = paintGroupNumber;
    }

    @Override
    public void run() {
        editorComponent.convertPaintGroupToSelection(paintGroupNumber);
    }

    public static PerformRunnableFactory getFactory(StickySelectionEditorComponent stickySelectionEditorComponent) {
        return new Factory(stickySelectionEditorComponent);
    }

    private static class Factory implements PerformRunnableFactory {
        private final StickySelectionEditorComponent stickySelectionEditorComponent;

        public Factory(StickySelectionEditorComponent stickySelectionEditorComponent) {
            this.stickySelectionEditorComponent = stickySelectionEditorComponent;
        }

        @Override
        public Runnable createPerformAction(int groupNumber) {
            return new PerformConvertToMultiCaretSelectionRunnable(stickySelectionEditorComponent, groupNumber);
        }
    }
}

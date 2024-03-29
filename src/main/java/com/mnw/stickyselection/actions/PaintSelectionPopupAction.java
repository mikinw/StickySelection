package com.mnw.stickyselection.actions;

import com.mnw.stickyselection.infrastructure.PaintGroupListPopupStep;
import com.mnw.stickyselection.infrastructure.PerformStickyPaintRunnable;
import org.jetbrains.annotations.NotNull;

public class PaintSelectionPopupAction extends ShowPopupAction {
    @Override
    protected void autoPerformPopupAction() {
        stickySelectionEditorComponent.paintSelection(0, false);
    }

    @NotNull
    @Override
    protected PaintGroupListPopupStep createListStep() {
        return new PaintGroupListPopupStep(
                "Paint selection and all similar",
                PerformStickyPaintRunnable.getFactory(stickySelectionEditorComponent, false));
    }
}

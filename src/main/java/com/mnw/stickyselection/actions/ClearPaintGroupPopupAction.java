package com.mnw.stickyselection.actions;

import com.mnw.stickyselection.infrastructure.PaintGroupListPopupStep;
import com.mnw.stickyselection.infrastructure.PerformClearRunnable;
import org.jetbrains.annotations.NotNull;

public class ClearPaintGroupPopupAction extends ShowPopupAction {
    @Override
    protected void autoPerformPopupAction() {
        stickySelectionEditorComponent.clearPaintGroup(0);
    }

    @NotNull
    @Override
    protected PaintGroupListPopupStep createListStep() {
        return new PaintGroupListPopupStep(
                "Clear Paint Group",
                PerformClearRunnable.getFactory(stickySelectionEditorComponent));
    }
}

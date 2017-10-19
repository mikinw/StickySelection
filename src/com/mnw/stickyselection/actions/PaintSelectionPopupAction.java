package com.mnw.stickyselection.actions;

import com.mnw.stickyselection.infrastructure.PaintGroupListPopupStep;
import com.mnw.stickyselection.infrastructure.PerformStickyRunnable;
import org.jetbrains.annotations.NotNull;

public class PaintSelectionPopupAction extends ShowPopupAction {
    @NotNull
    @Override
    protected PaintGroupListPopupStep createListStep() {
        return new PaintGroupListPopupStep("Paint selection", new PerformStickyRunnable(stickySelectionEditorComponent));
    }
}

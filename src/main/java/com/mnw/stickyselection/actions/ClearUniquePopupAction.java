package com.mnw.stickyselection.actions;

import com.mnw.stickyselection.infrastructure.PaintGroupListPopupStep;
import com.mnw.stickyselection.infrastructure.PerformClearUniqueRunnable;
import org.jetbrains.annotations.NotNull;

public class ClearUniquePopupAction extends ShowPopupAction {

    @Override
    protected void autoPerformPopupAction() {
        stickySelectionEditorComponent.clearAtCaretAllGroups();
    }

    @NotNull
    @Override
    protected PaintGroupListPopupStep createListStep() {
        return new PaintGroupListPopupStep(
                "Remove selection",
                PerformClearUniqueRunnable.getFactory(stickySelectionEditorComponent));
    }

    @Override
    protected boolean canPerformAutomatically() {
        return stickySelectionEditorComponent.isAtMostOneGroupAtCaret() || super.canPerformAutomatically();
    }
}

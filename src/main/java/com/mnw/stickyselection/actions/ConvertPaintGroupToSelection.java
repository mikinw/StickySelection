package com.mnw.stickyselection.actions;

import com.mnw.stickyselection.infrastructure.PaintGroupListPopupStep;
import com.mnw.stickyselection.infrastructure.PerformConvertToMultiCaretSelectionRunnable;
import org.jetbrains.annotations.NotNull;

public class ConvertPaintGroupToSelection extends ShowPopupAction {

    @Override
    protected void autoPerformPopupAction() {
        stickySelectionEditorComponent.convertPaintGroupToSelection(0);
    }

    @NotNull
    @Override
    protected PaintGroupListPopupStep createListStep() {
        return new PaintGroupListPopupStep(
                "Convert to Multi Caret Selection",
                PerformConvertToMultiCaretSelectionRunnable.getFactory(stickySelectionEditorComponent)
        );
    }
}

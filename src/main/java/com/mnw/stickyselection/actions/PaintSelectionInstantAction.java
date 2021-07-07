package com.mnw.stickyselection.actions;

import javax.swing.Icon;

public class PaintSelectionInstantAction extends StickyEditorAction {
    private final int paintGroup;

    public PaintSelectionInstantAction(int paintGroup, Icon icon) {
        super("Add selection to Paint Group " + paintGroup,
              "Add all occurrences of selected text to this Sticky Paint Group",
              icon);
        this.paintGroup = paintGroup;
    }

    @Override
    protected void actionImpl() {
        stickySelectionEditorComponent.paintSelection(paintGroup);
    }
}

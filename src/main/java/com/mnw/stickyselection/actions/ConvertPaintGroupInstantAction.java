package com.mnw.stickyselection.actions;

import javax.swing.Icon;

public class ConvertPaintGroupInstantAction extends StickyEditorAction {
    private final int paintGroup;

    public ConvertPaintGroupInstantAction(int paintGroup, Icon icon) {
        super("Convert Paint Group " + paintGroup + " to multi selection",
              "Every Sticky Selection in this group will be a selection",
              icon);
        this.paintGroup = paintGroup;
    }

    @Override
    protected void actionImpl() {
        stickySelectionEditorComponent.convertPaintGroupToSelection(paintGroup);
    }
}

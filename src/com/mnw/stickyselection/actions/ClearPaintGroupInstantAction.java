package com.mnw.stickyselection.actions;

import javax.swing.Icon;

public class ClearPaintGroupInstantAction extends StickyEditorAction {
    private final int paintGroup;

    public ClearPaintGroupInstantAction(int paintGroup, Icon icon) {
        super("Clear Paint Group " + paintGroup, "Removes all Sticky Selection of this group from the current editor", icon);
        this.paintGroup = paintGroup;
    }

    @Override
    protected void actionImpl() {
        stickySelectionEditorComponent.clearPaintGroup(paintGroup);
    }
}

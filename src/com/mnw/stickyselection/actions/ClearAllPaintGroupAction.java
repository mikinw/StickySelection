package com.mnw.stickyselection.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.mnw.stickyselection.StickySelectionAppComponent;

/**
 * TODO description of this class is missing
 */
public class ClearAllPaintGroupAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent actionEvent) {
        Editor editor = actionEvent.getData(PlatformDataKeys.EDITOR);
        Application application = ApplicationManager.getApplication();
        StickySelectionAppComponent identHighlightComp = application.getComponent(StickySelectionAppComponent.class);
        identHighlightComp.clearPaintGroup(editor, 0);
        identHighlightComp.clearPaintGroup(editor, 1);
        identHighlightComp.clearPaintGroup(editor, 2);
        identHighlightComp.clearPaintGroup(editor, 3);
        identHighlightComp.clearPaintGroup(editor, 4);
        identHighlightComp.clearPaintGroup(editor, 5);
        identHighlightComp.clearPaintGroup(editor, 6);
        identHighlightComp.clearPaintGroup(editor, 7);
    }
}

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
public class ClearPaintGroup7Action extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent actionEvent) {
        Editor editor = actionEvent.getData(PlatformDataKeys.EDITOR);
        Application application = ApplicationManager.getApplication();
        StickySelectionAppComponent identHighlightComp = application.getComponent(StickySelectionAppComponent.class);
        identHighlightComp.clearPaintGroup(editor, 0);
    }
}

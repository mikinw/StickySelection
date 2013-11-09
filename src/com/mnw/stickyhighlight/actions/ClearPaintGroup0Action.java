package com.mnw.stickyhighlight.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.mnw.stickyhighlight.StickyHighlightAppComponent;

/**
 * TODO description of this class is missing
 */
public class ClearPaintGroup0Action extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent ae) {
        Editor editor = (Editor)ae.getDataContext().getData(DataConstants.EDITOR);
        Application application = ApplicationManager.getApplication();
        StickyHighlightAppComponent identHighlightComp = application.getComponent(StickyHighlightAppComponent.class);
        identHighlightComp.clearPaintGroup(editor, 0);
    }

}

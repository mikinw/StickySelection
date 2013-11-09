package com.mnw.stickyhighlight.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;

/**
 * TODO description of this class is missing
 */
public class StickyHighlightAction extends AnAction {
    public void actionPerformed(AnActionEvent ae)
    {
        Editor editor = (Editor)ae.getDataContext().getData(DataConstants.EDITOR);
        Application application = ApplicationManager.getApplication();
//        IdentifierHighlighterAppComponent identHighlightComp = application.getComponent(IdentifierHighlighterAppComponent.class);
//        identHighlightComp.lockIdentifiers(editor);
    }

    public void update(AnActionEvent ae)
    {
        Editor editor = (Editor)ae.getDataContext().getData(DataConstants.EDITOR);
        Application application = ApplicationManager.getApplication();
//        IdentifierHighlighterAppComponent identHighlightComp = application.getComponent(IdentifierHighlighterAppComponent.class);
//        Presentation p = ae.getPresentation();
//        p.setEnabled(identHighlightComp.isPluginEnabled() && !identHighlightComp.areIdentifiersLocked(editor));
    }
}

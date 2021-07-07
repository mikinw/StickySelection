package com.mnw.stickyselection.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.mnw.stickyselection.StickySelectionAppComponent;
import com.mnw.stickyselection.StickySelectionEditorComponent;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

public abstract class StickyEditorAction extends AnAction {

    protected StickySelectionEditorComponent stickySelectionEditorComponent;

    public StickyEditorAction() {
        super();
    }
    public StickyEditorAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
        Application application = ApplicationManager.getApplication();
        StickySelectionAppComponent applicationComponent = application.getComponent(StickySelectionAppComponent.class);
        stickySelectionEditorComponent = applicationComponent
                .getStickySelectionEditorComponent(editor);

        if (stickySelectionEditorComponent == null) {
            return;
        }

        actionImpl();
    }

    protected abstract void actionImpl();
}

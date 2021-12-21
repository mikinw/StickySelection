package com.mnw.stickyselection.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.mnw.stickyselection.infrastructure.PaintGroupListPopupStep;
import com.mnw.stickyselection.model.ValuesRepository;
import org.jetbrains.annotations.NotNull;


public abstract class ShowPopupAction extends StickyEditorAction {

    private Editor editor;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        super.actionPerformed(anActionEvent);
    }

    @Override
    protected void actionImpl() {
        final int paintGroupCount = ServiceManager.getService(ValuesRepository.class).getPaintGroupCount();
        if (paintGroupCount > 1) {
            JBPopupFactory.getInstance()
                    .createListPopup(createListStep())
                    .showInBestPositionFor(editor);
        } else if (paintGroupCount == 1) {
            autoPerformPopupAction();
        } else {
            Notification notification = new Notification(
                    "Editor notifications",
                    "No Paint Group is defined",
                    "Please open Settings | Other | Sticky Selection <br> and add at least one.",
                    NotificationType.WARNING);
            Notifications.Bus.notify(notification);
        }

    }

    protected abstract void autoPerformPopupAction();

    @NotNull
    protected abstract PaintGroupListPopupStep createListStep();
}

package com.mnw.stickyselection.preferences;

import com.intellij.ui.JBColor;
import com.mnw.stickyselection.model.PaintGroupDataBean;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.event.ActionListener;

public class PaintGroupRow {
    private ColorButton colorButton;
    private JTextField textFieldShortcut;
    private JButton removeButton;
    private JCheckBox checkBoxMarker;
    private JTextField textFieldLayer;
    private JCheckBox checkBoxFrame;
    private JPanel rowPanel;
    private JButton buttonUp;

    private ActionListener removeListener;
    private ActionListener upListener;

    public PaintGroupRow() {

        ((AbstractDocument) textFieldLayer.getDocument()).setDocumentFilter(new OnlyNumbersDocumentFilter());

        ((AbstractDocument) textFieldShortcut.getDocument()).setDocumentFilter(new AtMostOneCharacterFilter());
    }

    public void setData(PaintGroupDataBean data) {
        textFieldShortcut.setText(data.getShortcut());
        checkBoxFrame.setSelected(data.isFrameNeeded());
        textFieldLayer.setText(String.valueOf(data.getHighlightLayer()));
        checkBoxMarker.setSelected(data.isMarkerNeeded());
        colorButton.setColor(data.getColor());
    }

    public PaintGroupDataBean getData() {
        final PaintGroupDataBean paintGroupDataBean = new PaintGroupDataBean();

        final int layer = textFieldLayer.getText().isEmpty() ? 0 : Integer.parseInt(textFieldLayer.getText());
        paintGroupDataBean.setShortcut(textFieldShortcut.getText());
        paintGroupDataBean.setFrameNeeded(checkBoxFrame.isSelected());
        paintGroupDataBean.setHighlightLayer(layer);
        paintGroupDataBean.setMarkerNeeded(checkBoxMarker.isSelected());
        paintGroupDataBean.setColor(colorButton.getColor());

        return paintGroupDataBean;
    }


    private void createUIComponents() {
        colorButton = new ColorButton();
        colorButton.setColor(JBColor.WHITE);
    }

    public void addRemoveClickListener(final ActionListener listener) {
        removeButton.removeActionListener(removeListener);
        removeButton.addActionListener(listener);
        removeListener = listener;

    }

    public void addUpClickListener(final ActionListener listener) {
        buttonUp.removeActionListener(upListener);
        upListener = listener;
        buttonUp.addActionListener(listener);

    }



    public JComponent getRootComponent() { return rowPanel; }

    public void setUpEnabled(final boolean b) {
            buttonUp.setEnabled(b);
    }
}

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

    public PaintGroupRow() {

        ((AbstractDocument) textFieldLayer.getDocument()).setDocumentFilter(new OnlyNumbersDocumentFilter());

        ((AbstractDocument) textFieldShortcut.getDocument()).setDocumentFilter(new OnlyOneCharacterFilter());
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

        paintGroupDataBean.setShortcut(textFieldShortcut.getText());
        paintGroupDataBean.setFrameNeeded(checkBoxFrame.isSelected());
        paintGroupDataBean.setHighlightLayer(Integer.parseInt(textFieldLayer.getText()));
        paintGroupDataBean.setMarkerNeeded(checkBoxMarker.isSelected());
        paintGroupDataBean.setColor(colorButton.getColor());

        return paintGroupDataBean;
    }


    private void createUIComponents() {
        colorButton = new ColorButton();
        colorButton.setColor(JBColor.WHITE);
    }

    public void addRemoveClickListener(final ActionListener listener) {
        removeButton.addActionListener(listener);

    }



    public JComponent getRootComponent() { return rowPanel; }
}

package com.mnw.stickyselection.preferences;

import com.mnw.stickyselection.model.PaintGroupDataBean;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionListener;

public class PaintGroupRow {
    private ColorButton colorButton;
    private JTextField textFieldShortcut;
    private JButton removeButton;
    private JCheckBox checkBoxMarker;
    private JTextField textFieldLayer;
    private JCheckBox checkBoxFrame;
    private JPanel rowPanel;

    private int dataBeanId;

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
        dataBeanId = data.getId();
    }

    public PaintGroupDataBean getData() {
        final PaintGroupDataBean paintGroupDataBean = new PaintGroupDataBean();

        paintGroupDataBean.setShortcut(textFieldShortcut.getText());
        paintGroupDataBean.setFrameNeeded(checkBoxFrame.isSelected());
        paintGroupDataBean.setLayer(Integer.parseInt(textFieldLayer.getText()));
        paintGroupDataBean.setMarkerNeeded(checkBoxMarker.isSelected());
        paintGroupDataBean.setColor(colorButton.getColor());

        return paintGroupDataBean;
    }

    public int getDataBeanId() {
        return dataBeanId;
    }

    public void linkToDataBean(PaintGroupDataBean data) {
//        getData(data);
        this.dataBeanId = data.getId();
    }

    public boolean isModified(PaintGroupDataBean data) {
        if (textFieldShortcut.getText() != null
                ? !textFieldShortcut.getText().equals(data.getShortcut())
                : data.getShortcut() != null) {
            return true;
        }
        if (checkBoxFrame.isSelected() != data.isFrameNeeded()) {
            return true;
        }
        if (!textFieldLayer.getText().equals(String.valueOf(data.getHighlightLayer()))) {
            return true;
        }
        if (checkBoxMarker.isSelected() != data.isMarkerNeeded()) {
            return true;
        }
        if (colorButton.getColor() != null
                ? !colorButton.getColor().equals(data.getColor())
                : data.getColor() != null) {
            return true;
        }
        return false;
    }

    private void createUIComponents() {
        colorButton = new ColorButton();
        colorButton.setColor(Color.white);
    }

    public void addRemoveClickListener(final ActionListener listener) {
        removeButton.addActionListener(listener);

    }



    public JComponent getRootComponent() { return rowPanel; }
}

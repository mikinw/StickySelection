package com.mnw.stickyselection.preferences;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.mnw.stickyselection.model.PaintGroupDataBean;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Random;

public class PaintGroupRow {
    private ColorButton colorButton;
    private JTextField textFieldShortcut;
    private JButton removeButton;
    private JCheckBox checkBoxMarker;
    private JTextField textFieldLayer;
    private JCheckBox checkBoxFrame;
    private JPanel rowPanel;

    public PaintGroupRow() {
        $$$setupUI$$$();

        final OnlyNumbersDocumentFilter onlyNumbersDocumentFilter = new OnlyNumbersDocumentFilter();
        ((AbstractDocument) textFieldLayer.getDocument()).setDocumentFilter(onlyNumbersDocumentFilter);

        ((AbstractDocument) textFieldShortcut.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string,
                                     AttributeSet attr) throws BadLocationException {
                fb.replace(0, fb.getDocument().getLength(), string.substring(0, 1).toUpperCase(), attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text,
                                AttributeSet attrs) throws BadLocationException {
                super.replace(fb, 0, fb.getDocument().getLength(), text.substring(0, 1).toUpperCase(), attrs);
            }
        });
    }

    public void setData(PaintGroupDataBean data) {
        textFieldShortcut.setText(data.getShortcut());
        checkBoxFrame.setSelected(data.isFrameNeeded());
        textFieldLayer.setText(String.valueOf(data.getHughlightLayer()));
        checkBoxMarker.setSelected(data.isMarkerNeeded());
        colorButton.setColor(data.getColor());
    }

    public void getData(PaintGroupDataBean data) {
        data.setShortcut(textFieldShortcut.getText());
        data.setFrameNeeded(checkBoxFrame.isSelected());
        data.setLayer(Integer.parseUnsignedInt(textFieldLayer.getText()));
        data.setMarkerNeeded(checkBoxMarker.isSelected());
        data.setColor(colorButton.getColor());
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
        if (!textFieldLayer.getText().equals(String.valueOf(data.getHughlightLayer()))) {
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
        Random random = new Random((new Date()).getTime());
        colorButton = new ColorButton();
        colorButton.setColor(Color.getHSBColor(random.nextFloat(), random.nextFloat(), random.nextFloat()));
    }

    public void addRemoveClickListener(final ActionListener listener) {
        removeButton.addActionListener(listener);

    }


    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        rowPanel = new JPanel();
        rowPanel.setLayout(new GridLayoutManager(2, 8, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Paint Group Shortcut");
        rowPanel.add(label1,
                     new GridConstraints(0,
                                         0,
                                         1,
                                         1,
                                         GridConstraints.ANCHOR_WEST,
                                         GridConstraints.FILL_NONE,
                                         1,
                                         GridConstraints.SIZEPOLICY_FIXED,
                                         null,
                                         new Dimension(122, 21),
                                         null,
                                         0,
                                         false));
        textFieldShortcut = new JTextField();
        textFieldShortcut.setColumns(1);
        textFieldShortcut.setText("2");
        textFieldShortcut
                .setToolTipText("When the Paint Group popup is shown you can press this button to select this one.");
        rowPanel.add(textFieldShortcut,
                     new GridConstraints(0,
                                         1,
                                         1,
                                         1,
                                         GridConstraints.ANCHOR_CENTER,
                                         GridConstraints.FILL_NONE,
                                         GridConstraints.SIZEPOLICY_FIXED,
                                         GridConstraints.SIZEPOLICY_FIXED,
                                         null,
                                         new Dimension(74, 31),
                                         new Dimension(50, -1),
                                         0,
                                         false));
        colorButton.setToolTipText("Color of the highlight after you added a text to the paint group");
        rowPanel.add(colorButton,
                     new GridConstraints(0,
                                         2,
                                         1,
                                         1,
                                         GridConstraints.ANCHOR_CENTER,
                                         GridConstraints.FILL_NONE,
                                         GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                         GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                         null,
                                         new Dimension(80, 20),
                                         null,
                                         0,
                                         false));
        checkBoxFrame = new JCheckBox();
        checkBoxFrame.setText("Show frame");
        checkBoxFrame.setToolTipText(
                "This can come handy, when you wish to see if something is in the paint group even when it is "
                        + "selected.");
        rowPanel.add(checkBoxFrame,
                     new GridConstraints(0,
                                         3,
                                         1,
                                         1,
                                         GridConstraints.ANCHOR_WEST,
                                         GridConstraints.FILL_NONE,
                                         GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
                                         GridConstraints.SIZEPOLICY_FIXED,
                                         null,
                                         null,
                                         null,
                                         0,
                                         false));
        textFieldLayer = new JTextField();
        textFieldLayer.setText("2000");
        textFieldLayer.setToolTipText(
                "Priority order of this highlight. Higher priorities will cover lower ones. \nHint from the IntelliJ "
                        + "sources: CARET_ROW = 1000; SYNTAX = 2000; ADDITIONAL_SYNTAX = 3000; GUARDED_BLOCKS = 3500;"
                        + " WARNING = 4000; ERROR = 5000; SELECTION = 6000;");
        rowPanel.add(textFieldLayer,
                     new GridConstraints(0,
                                         5,
                                         1,
                                         1,
                                         GridConstraints.ANCHOR_WEST,
                                         GridConstraints.FILL_NONE,
                                         GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                         GridConstraints.SIZEPOLICY_FIXED,
                                         null,
                                         new Dimension(70, 31),
                                         null,
                                         0,
                                         false));
        checkBoxMarker = new JCheckBox();
        checkBoxMarker.setText("Show in marker bar");
        checkBoxMarker.setToolTipText("Show a marker in the marker bar to the right");
        rowPanel.add(checkBoxMarker,
                     new GridConstraints(0,
                                         6,
                                         1,
                                         1,
                                         GridConstraints.ANCHOR_CENTER,
                                         GridConstraints.FILL_NONE,
                                         GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
                                         GridConstraints.SIZEPOLICY_FIXED,
                                         null,
                                         null,
                                         null,
                                         0,
                                         false));
        removeButton = new JButton();
        removeButton.setActionCommand("RemovePaintGroup");
        removeButton.setText("Remove");
        rowPanel.add(removeButton,
                     new GridConstraints(0,
                                         7,
                                         1,
                                         1,
                                         GridConstraints.ANCHOR_EAST,
                                         GridConstraints.FILL_NONE,
                                         GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                         GridConstraints.SIZEPOLICY_FIXED,
                                         null,
                                         null,
                                         null,
                                         0,
                                         false));
        final JLabel label2 = new JLabel();
        label2.setText("Highlight layer");
        rowPanel.add(label2,
                     new GridConstraints(0,
                                         4,
                                         1,
                                         1,
                                         GridConstraints.ANCHOR_WEST,
                                         GridConstraints.FILL_NONE,
                                         GridConstraints.SIZEPOLICY_FIXED,
                                         GridConstraints.SIZEPOLICY_FIXED,
                                         null,
                                         null,
                                         null,
                                         0,
                                         false));
    }

    /** @noinspection ALL */
    public JComponent $$$getRootComponent$$$() { return rowPanel; }
}
package com.mnw.stickyhighlight.preferences;

import com.mnw.stickyhighlight.model.DefaultValues;
import com.mnw.stickyhighlight.model.ValuesRepository;
import com.mnw.stickyhighlight.model.ValuesRepositoryReader;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.text.AbstractDocument;

/**
 * TODO description of this class is missing
 */
public class StickySelectionPreferences {

    private static final int NUMBER_OF_SELECTION_GROUPS = 8;

    private JPanel mainPanel;

    private JTextField[] textFieldsLayers;
    private JTextField textAreaHighlightLayerForSelectionGroup0;
    private JTextField textAreaHighlightLayerForSelectionGroup1;
    private JTextField textAreaHighlightLayerForSelectionGroup2;
    private JTextField textAreaHighlightLayerForSelectionGroup3;
    private JTextField textAreaHighlightLayerForSelectionGroup4;
    private JTextField textAreaHighlightLayerForSelectionGroup5;
    private JTextField textAreaHighlightLayerForSelectionGroup6;
    private JTextField textAreaHighlightLayerForSelectionGroup7;

    private JCheckBox[] checkBoxesMarkers;
    private JCheckBox checkBoxMarkerForSelectionGroup0;
    private JCheckBox checkBoxMarkerForSelectionGroup1;
    private JCheckBox checkBoxMarkerForSelectionGroup2;
    private JCheckBox checkBoxMarkerForSelectionGroup3;
    private JCheckBox checkBoxMarkerForSelectionGroup4;
    private JCheckBox checkBoxMarkerForSelectionGroup5;
    private JCheckBox checkBoxMarkerForSelectionGroup6;
    private JCheckBox checkBoxMarkerForSelectionGroup7;

    private ColorButton[] buttonsColors;
    private ColorButton buttonSetSelectionGroup0Color;
    private ColorButton buttonSetSelectionGroup1Color;
    private ColorButton buttonSetSelectionGroup2Color;
    private ColorButton buttonSetSelectionGroup3Color;
    private ColorButton buttonSetSelectionGroup4Color;
    private ColorButton buttonSetSelectionGroup5Color;
    private ColorButton buttonSetSelectionGroup6Color;
    private ColorButton buttonSetSelectionGroup7Color;

    private JButton[] buttonsReset;
    private JButton buttonResetSelectionGroup0;
    private JButton buttonResetSelectionGroup1;
    private JButton buttonResetSelectionGroup2;
    private JButton buttonResetSelectionGroup3;
    private JButton buttonResetSelectionGroup4;
    private JButton buttonResetSelectionGroup5;
    private JButton buttonResetSelectionGroup6;
    private JButton buttonResetSelectionGroup7;

    private JCheckBox checkBoxPluginEnabled;

    private JPanel panelColorScheme;


    private ValuesRepositoryReader defaultValues;
    private ValuesRepositoryReader setData;

    public StickySelectionPreferences() {
        buttonsReset = new JButton[NUMBER_OF_SELECTION_GROUPS];
        textFieldsLayers = new JTextField[NUMBER_OF_SELECTION_GROUPS];
        buttonsColors = new ColorButton[NUMBER_OF_SELECTION_GROUPS];
        checkBoxesMarkers = new JCheckBox[NUMBER_OF_SELECTION_GROUPS];

        setupUI();
        defaultValues = new DefaultValues()
        ;
        buttonSetSelectionGroup0Color
                .addActionListener(new PreferencesSetColorActionListener("Selection Group #0 Color"));
        buttonSetSelectionGroup1Color
                .addActionListener(new PreferencesSetColorActionListener("Selection Group #1 Color"));
        buttonSetSelectionGroup2Color
                .addActionListener(new PreferencesSetColorActionListener("Selection Group #2 Color"));
        buttonSetSelectionGroup3Color
                .addActionListener(new PreferencesSetColorActionListener("Selection Group #3 Color"));
        buttonSetSelectionGroup4Color
                .addActionListener(new PreferencesSetColorActionListener("Selection Group #4 Color"));
        buttonSetSelectionGroup5Color
                .addActionListener(new PreferencesSetColorActionListener("Selection Group #5 Color"));
        buttonSetSelectionGroup6Color
                .addActionListener(new PreferencesSetColorActionListener("Selection Group #6 Color"));
        buttonSetSelectionGroup7Color
                .addActionListener(new PreferencesSetColorActionListener("Selection Group #7 Color"));

        buttonResetSelectionGroup0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                resetSelectionGroupToDefault(1);
            }
        });
        buttonResetSelectionGroup1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                resetSelectionGroupToDefault(2);
            }
        });
        buttonResetSelectionGroup2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                resetSelectionGroupToDefault(3);
            }
        });
        buttonResetSelectionGroup3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                resetSelectionGroupToDefault(4);
            }
        });
        buttonResetSelectionGroup4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                resetSelectionGroupToDefault(5);
            }
        });
        buttonResetSelectionGroup5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                resetSelectionGroupToDefault(6);
            }
        });
        buttonResetSelectionGroup6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                resetSelectionGroupToDefault(7);
            }
        });
        buttonResetSelectionGroup7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                resetSelectionGroupToDefault(8);
            }
        });

        checkBoxPluginEnabled.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                updatePluginEnabled();
            }
        });
    }

    private void resetSelectionGroupToDefault(int groupNumber) {
        setDataForSelectionGroupFrom(defaultValues, groupNumber);
    }

    private void setDataForSelectionGroupFrom(ValuesRepositoryReader repository, int groupNumber) {
        buttonsColors[groupNumber].setColor(repository.getColorOfSelectionGroup(groupNumber));
        textFieldsLayers[groupNumber].setText(String.valueOf(repository.getHighlightLayerOfSelectionGroup(groupNumber)));
    }

    protected void updatePluginEnabled() {
        setHierarchyEnabled(panelColorScheme, checkBoxPluginEnabled.isSelected());
    }

    protected void setHierarchyEnabled(Component comp, boolean enabled) {
        comp.setEnabled(enabled);
        Component children[] = new Component[0];
        if (comp instanceof Container) {
            children = ((Container) comp).getComponents();
        }
        for (Component child : children) {
            setHierarchyEnabled(child, enabled);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setData(ValuesRepositoryReader savedValues) {
        setData = savedValues;
        for (int i = 0; i < NUMBER_OF_SELECTION_GROUPS; i++) {
            setDataForSelectionGroupFrom(savedValues, i);
        }
        checkBoxPluginEnabled.setSelected(savedValues.isPluginEnabled());
        updatePluginEnabled();
    }

    public boolean isModified() {
//        System.out.println("isModified()");

        for (int i = 0; i < NUMBER_OF_SELECTION_GROUPS; i++) {

            final Color colorOfSelectionGroup = setData.getColorOfSelectionGroup(i);
            final Color currentColor = buttonsColors[i].getColor();
            if (!currentColor.equals(colorOfSelectionGroup)) {
//                System.out.println("color different " + buttonsColors[i].getColor() + " " + setData.getColorOfSelectionGroup(i));
                return true;
            }
            if (checkBoxesMarkers[i].isSelected() != setData.isMarkerEnabledForSelectionGroup(i)) {
//                System.out.println("checkMarker different");
                return true;
            }
            final String highlightLayerString = String.valueOf(setData.getHighlightLayerOfSelectionGroup(i));
            if (!textFieldsLayers[i].getText().equals(highlightLayerString)) {
//                System.out.println("highlight layer different " + textFieldsLayers[i].getText() + " " + highlightLayerString);
                return true;
            }
        }
//        System.out.println("return false");

        return false;
    }

    public ValuesRepositoryReader getData() {
        ValuesRepository ret = new ValuesRepository();

        for (int i = 0; i < NUMBER_OF_SELECTION_GROUPS; i++) {
            ret.setColorOfSelectionGroup(i, buttonsColors[i].getColor());
            int highlightLayer;
            try {
                highlightLayer = Integer.parseInt(textFieldsLayers[i].getText());
            } catch (NumberFormatException e) {
                highlightLayer = defaultValues.getHighlightLayerOfSelectionGroup(i);
            }
            ret.setHighlightLayerOfSelectionGroup(i, highlightLayer);
            ret.setIsMarkerEnabledForSelectionGroup(i, checkBoxesMarkers[i].isSelected());
        }
        ret.setIsPluginEnabled(checkBoxPluginEnabled.isEnabled());

        return ret;
    }

/*

    protected Color getColorFromColorButton(JButton b) {
        ImageIcon ii = (ImageIcon) b.getIcon();
        ColorImage ci = (ColorImage) ii.getImage();
        return (ci.getColor());
    }

    protected String getStringFromColorButton(JButton b) {
        Color c = getColorFromColorButton(b);
        return (getStringFromColor(c));
    }

    protected void setColorToColorButton(JButton b, Color c) {
        b.setIcon(new ImageIcon(new ColorImage(PREFERRED_WIDTH, PREFERRED_HEIGHT, c)));
    }

    protected void setStringToColorButton(JButton b, String s) {
        Color c = getColorFromString(s);
        setColorToColorButton(b, c);
    }

    public static String getStringFromColor(Color c) {
        return ("" + c.getRed() + "," + c.getGreen() + "," + c.getBlue());
    }

    public static Color getColorFromString(String s) {
        StringTokenizer tokens = new StringTokenizer(s, ",");
        if (tokens.countTokens() == 3) {
            String redToken = tokens.nextToken();
            String greenToken = tokens.nextToken();
            String blueToken = tokens.nextToken();
            try {
                int red = Integer.parseInt(redToken);
                int green = Integer.parseInt(greenToken);
                int blue = Integer.parseInt(blueToken);
                if ((red >= 0) && (red <= 255) && (green >= 0) && (green <= 255) && (blue >= 0) && (blue <= 255)) {
                    Color retVal = new Color(red, green, blue);
                    return (retVal);
                }
            } catch (NumberFormatException nfe) {
                //Ignore
            }
        }
        return (null);
    }

*/
    // region Create UI


    private void setupUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        panelColorScheme = new JPanel();
        final LayoutManager gridLayoutManager = new GridBagLayout();
        panelColorScheme.setLayout(gridLayoutManager);
        mainPanel.add(panelColorScheme, BorderLayout.CENTER);
        panelColorScheme.setBorder(BorderFactory.createTitledBorder("Color Scheme"));
        final JLabel label1 = new JLabel();
        label1.setFont(new Font(label1.getFont().getName(), Font.BOLD, label1.getFont().getSize()));
        label1.setText("Highlight Layer");
        panelColorScheme.add(label1,
                             new GridBagConstraints(1,
                                                    0,
                                                    1,
                                                    1,
                                                    1f,
                                                    1f,
                                                    GridBagConstraints.CENTER,
                                                    GridBagConstraints.NONE,
                                                    new Insets(0, 0, 0, 0),
                                                    0,
                                                    0));
        final JTextArea label2 = new JTextArea();
        label2.setEditable(false);
        label2.setLineWrap(true);
        label2.setOpaque(false);
        label2.setBorder(BorderFactory.createEmptyBorder());
        label2.setFont(new Font(label2.getFont().getName(), Font.BOLD, label2.getFont().getSize()));
        label2.setText("Show in \nMarker Bar");
        panelColorScheme.add(label2,
                             new GridBagConstraints(2,
                                                    0,
                                                    1,
                                                    1,
                                                    1f,
                                                    1f,
                                                    GridBagConstraints.CENTER,
                                                    GridBagConstraints.NONE,
                                                    new Insets(0, 0, 0, 0),
                                                    0,
                                                    0));
        final JLabel label3 = new JLabel();
        label3.setFont(new Font(label3.getFont().getName(), Font.BOLD, label3.getFont().getSize()));
        label3.setText("Color");
        panelColorScheme.add(label3,
                             new GridBagConstraints(3,
                                                    0,
                                                    1,
                                                    1,
                                                    1f,
                                                    1f,
                                                    GridBagConstraints.CENTER,
                                                    GridBagConstraints.NONE,
                                                    new Insets(0, 0, 0, 0),
                                                    0,
                                                    0));
        addTextTo(0, 1, "Selection Group #0");
        addTextTo(0, 2, "Selection Group #1");
        addTextTo(0, 3, "Selection Group #2");
        addTextTo(0, 4, "Selection Group #3");
        addTextTo(0, 5, "Selection Group #4");
        addTextTo(0, 6, "Selection Group #5");
        addTextTo(0, 7, "Selection Group #6");
        addTextTo(0, 8, "Selection Group #7");

        final OnlyNumbersDocumentFilter onlyNumbersDocumentFilter = new OnlyNumbersDocumentFilter();
        textAreaHighlightLayerForSelectionGroup0 = createHighlightLayerTextFiled(onlyNumbersDocumentFilter);
        addTextAreaToPanelAt(1, 1, textAreaHighlightLayerForSelectionGroup0);
        textAreaHighlightLayerForSelectionGroup1 = createHighlightLayerTextFiled(onlyNumbersDocumentFilter);
        addTextAreaToPanelAt(1, 2, textAreaHighlightLayerForSelectionGroup1);
        textAreaHighlightLayerForSelectionGroup2 = createHighlightLayerTextFiled(onlyNumbersDocumentFilter);
        addTextAreaToPanelAt(1, 3, textAreaHighlightLayerForSelectionGroup2);
        textAreaHighlightLayerForSelectionGroup3 = createHighlightLayerTextFiled(onlyNumbersDocumentFilter);
        addTextAreaToPanelAt(1, 4, textAreaHighlightLayerForSelectionGroup3);
        textAreaHighlightLayerForSelectionGroup4 = createHighlightLayerTextFiled(onlyNumbersDocumentFilter);
        addTextAreaToPanelAt(1, 5, textAreaHighlightLayerForSelectionGroup4);
        textAreaHighlightLayerForSelectionGroup5 = createHighlightLayerTextFiled(onlyNumbersDocumentFilter);
        addTextAreaToPanelAt(1, 6, textAreaHighlightLayerForSelectionGroup5);
        textAreaHighlightLayerForSelectionGroup6 = createHighlightLayerTextFiled(onlyNumbersDocumentFilter);
        addTextAreaToPanelAt(1, 7, textAreaHighlightLayerForSelectionGroup6);
        textAreaHighlightLayerForSelectionGroup7 = createHighlightLayerTextFiled(onlyNumbersDocumentFilter);
        addTextAreaToPanelAt(1, 8, textAreaHighlightLayerForSelectionGroup7);

        checkBoxMarkerForSelectionGroup0 = createMarkerCheckBox();
        addCheckBoxToPanelAt(2, 1, checkBoxMarkerForSelectionGroup0);
        checkBoxMarkerForSelectionGroup1 = createMarkerCheckBox();
        addCheckBoxToPanelAt(2, 2, checkBoxMarkerForSelectionGroup1);
        checkBoxMarkerForSelectionGroup2 = createMarkerCheckBox();
        addCheckBoxToPanelAt(2, 3, checkBoxMarkerForSelectionGroup2);
        checkBoxMarkerForSelectionGroup3 = createMarkerCheckBox();
        addCheckBoxToPanelAt(2, 4, checkBoxMarkerForSelectionGroup3);
        checkBoxMarkerForSelectionGroup4 = createMarkerCheckBox();
        addCheckBoxToPanelAt(2, 5, checkBoxMarkerForSelectionGroup4);
        checkBoxMarkerForSelectionGroup5 = createMarkerCheckBox();
        addCheckBoxToPanelAt(2, 6, checkBoxMarkerForSelectionGroup5);
        checkBoxMarkerForSelectionGroup6 = createMarkerCheckBox();
        addCheckBoxToPanelAt(2, 7, checkBoxMarkerForSelectionGroup6);
        checkBoxMarkerForSelectionGroup7 = createMarkerCheckBox();
        addCheckBoxToPanelAt(2, 8, checkBoxMarkerForSelectionGroup7);

        buttonSetSelectionGroup0Color = new ColorButton();
        buttonSetSelectionGroup0Color.setText("");
        buttonSetSelectionGroup0Color.setToolTipText("Set color for Selection Group #0");
        addColorButtonToPanelAt(3, 1, buttonSetSelectionGroup0Color);
        buttonSetSelectionGroup1Color = new ColorButton();
        buttonSetSelectionGroup1Color.setText("");
        buttonSetSelectionGroup1Color.setToolTipText("Set color for Selection Group #1");
        addColorButtonToPanelAt(3, 2, buttonSetSelectionGroup1Color);
        buttonSetSelectionGroup2Color = new ColorButton();
        buttonSetSelectionGroup2Color.setText("");
        buttonSetSelectionGroup2Color.setToolTipText("Set color for Selection Group #2");
        addColorButtonToPanelAt(3, 3, buttonSetSelectionGroup2Color);
        buttonSetSelectionGroup3Color = new ColorButton();
        buttonSetSelectionGroup3Color.setText("");
        buttonSetSelectionGroup3Color.setToolTipText("Set color for Selection Group #3");
        addColorButtonToPanelAt(3, 4, buttonSetSelectionGroup3Color);
        buttonSetSelectionGroup4Color = new ColorButton();
        buttonSetSelectionGroup4Color.setText("");
        buttonSetSelectionGroup4Color.setToolTipText("Set color for Selection Group #4");
        addColorButtonToPanelAt(3, 5, buttonSetSelectionGroup4Color);
        buttonSetSelectionGroup5Color = new ColorButton();
        buttonSetSelectionGroup5Color.setText("");
        buttonSetSelectionGroup5Color.setToolTipText("Set color for Selection Group #5");
        addColorButtonToPanelAt(3, 6, buttonSetSelectionGroup5Color);
        buttonSetSelectionGroup6Color = new ColorButton();
        buttonSetSelectionGroup6Color.setText("");
        buttonSetSelectionGroup6Color.setToolTipText("Set color for Selection Group #6");
        addColorButtonToPanelAt(3, 7, buttonSetSelectionGroup6Color);
        buttonSetSelectionGroup7Color = new ColorButton();
        buttonSetSelectionGroup7Color.setText("");
        buttonSetSelectionGroup7Color.setToolTipText("Set color for Selection Group #7");
        addColorButtonToPanelAt(3, 8, buttonSetSelectionGroup7Color);


        buttonResetSelectionGroup0 = new JButton();
        buttonResetSelectionGroup0.setText("Default");
        buttonResetSelectionGroup0.setToolTipText("Set color and highlight layer to default");
        addResetButtonToPanelAt(4, 1, buttonResetSelectionGroup0);
        buttonResetSelectionGroup1 = new JButton();
        buttonResetSelectionGroup1.setText("Default");
        buttonResetSelectionGroup1.setToolTipText("Set color and highlight layer to default");
        addResetButtonToPanelAt(4, 2, buttonResetSelectionGroup1);
        buttonResetSelectionGroup2 = new JButton();
        buttonResetSelectionGroup2.setText("Default");
        buttonResetSelectionGroup2.setToolTipText("Set color and highlight layer to default");
        addResetButtonToPanelAt(4, 3, buttonResetSelectionGroup2);
        buttonResetSelectionGroup3 = new JButton();
        buttonResetSelectionGroup3.setText("Default");
        buttonResetSelectionGroup3.setToolTipText("Set color and highlight layer to default");
        addResetButtonToPanelAt(4, 4, buttonResetSelectionGroup3);
        buttonResetSelectionGroup4 = new JButton();
        buttonResetSelectionGroup4.setText("Default");
        buttonResetSelectionGroup4.setToolTipText("Set color and highlight layer to default");
        addResetButtonToPanelAt(4, 5, buttonResetSelectionGroup4);
        buttonResetSelectionGroup5 = new JButton();
        buttonResetSelectionGroup5.setText("Default");
        buttonResetSelectionGroup5.setToolTipText("Set color and highlight layer to default");
        addResetButtonToPanelAt(4, 6, buttonResetSelectionGroup5);
        buttonResetSelectionGroup6 = new JButton();
        buttonResetSelectionGroup6.setText("Default");
        buttonResetSelectionGroup6.setToolTipText("Set color and highlight layer to default");
        addResetButtonToPanelAt(4, 7, buttonResetSelectionGroup6);
        buttonResetSelectionGroup7 = new JButton();
        buttonResetSelectionGroup7.setText("Default");
        buttonResetSelectionGroup7.setToolTipText("Set color and highlight layer to default");
        addResetButtonToPanelAt(4, 8, buttonResetSelectionGroup7);

        final JLabel label12 = new JLabel();
        label12.setOpaque(false);
        label12.setText(
                "<html><b>Hint:</b>  <br>"
                        + "  CARET_ROW = 1000; <br>SYNTAX = 2000; <br>"
                        + "  ADDITIONAL_SYNTAX = 3000; <br>GUARDED_BLOCKS = 3500; <br>"
                        + "  WARNING = 4000; <br>ERROR = 5000; <br>SELECTION = 6000; <br>"
                        + "Priority order which to draw on top of the other (including the other markers).</html>");
        panelColorScheme.add(label12,
                             new GridBagConstraints(1,
                                                    9,
                                                    4,
                                                    1,
                                                    1f,
                                                    1f,
                                                    GridBagConstraints.WEST,
                                                    GridBagConstraints.NONE,
                                                    new Insets(0, 0, 0, 0),
                                                    0,
                                                    0));
        checkBoxPluginEnabled = new JCheckBox();
        checkBoxPluginEnabled.setSelected(true);
        checkBoxPluginEnabled.setText("Plugin Enabled");
        mainPanel.add(checkBoxPluginEnabled, BorderLayout.NORTH);
    }

    private void addResetButtonToPanelAt(int x, int y, JButton resetButton) {
        panelColorScheme.add(resetButton,
                             new GridBagConstraints(x,
                                                    y,
                                                    1,
                                                    1,
                                                    1f,
                                                    1f,
                                                    GridBagConstraints.CENTER,
                                                    GridBagConstraints.NONE,
                                                    new Insets(0, 0, 0, 0),
                                                    0,
                                                    0));
        buttonsReset[y-1] = resetButton;
    }

    private void addColorButtonToPanelAt(int x, int y, ColorButton button) {
        panelColorScheme.add(button,
                             new GridBagConstraints(x,
                                                    y,
                                                    1,
                                                    1,
                                                    1f,
                                                    1f,
                                                    GridBagConstraints.CENTER,
                                                    GridBagConstraints.NONE,
                                                    new Insets(0, 0, 0, 0),
                                                    0,
                                                    0));
        buttonsColors[y-1] = button;
    }

    private JCheckBox createMarkerCheckBox() {
        JCheckBox ret = new JCheckBox();
        ret.setSelected(true);
        ret.setText("");
        return ret;
    }

    private void addCheckBoxToPanelAt(int x, int y, JCheckBox checkBox) {
        panelColorScheme.add(checkBox,
                             new GridBagConstraints(x,
                                                    y,
                                                    1,
                                                    1,
                                                    1f,
                                                    1f,
                                                    GridBagConstraints.CENTER,
                                                    GridBagConstraints.NONE,
                                                    new Insets(0, 0, 0, 0),
                                                    0,
                                                    0));
        checkBoxesMarkers[y-1] = checkBox;
    }

    private void addTextTo(int x, int y, String text) {
        final JLabel label4 = new JLabel();
        label4.setText(text);
        panelColorScheme.add(label4,
                             new GridBagConstraints(x,
                                                    y,
                                                    1,
                                                    1,
                                                    1f,
                                                    1f,
                                                    GridBagConstraints.WEST,
                                                    GridBagConstraints.NONE,
                                                    new Insets(0, 0, 0, 0),
                                                    0,
                                                    0));
    }

    private void addTextAreaToPanelAt(int x, int y, JTextField textArea) {
        panelColorScheme.add(textArea,
                             new GridBagConstraints(x,
                                                    y,
                                                    1,
                                                    1,
                                                    1f,
                                                    1f,
                                                    GridBagConstraints.CENTER,
                                                    GridBagConstraints.HORIZONTAL,
                                                    new Insets(0, 0, 0, 0),
                                                    0,
                                                    0));
        textFieldsLayers[y-1] = textArea;
    }

    private JTextField createHighlightLayerTextFiled(OnlyNumbersDocumentFilter onlyNumbersDocumentFilter) {
        JTextField ret = new JTextField();
        ((AbstractDocument)ret.getDocument()).setDocumentFilter(onlyNumbersDocumentFilter);
        return ret;
    }

    // endregion Create UI

//    /** @noinspection ALL */
//    public JComponent $$$getRootComponent$$$() { return mainPanel; }
}

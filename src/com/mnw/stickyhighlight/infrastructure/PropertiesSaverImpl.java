package com.mnw.stickyhighlight.infrastructure;

import com.intellij.ide.util.PropertiesComponent;
import com.mnw.stickyhighlight.model.PropertiesSaver;
import com.mnw.stickyhighlight.model.ValuesRepositoryReader;

import java.awt.*;

/**
 * TODO description of this class is missing
 */
public class PropertiesSaverImpl implements PropertiesSaver {
    private final PropertiesComponent propertiesComponent;

    public PropertiesSaverImpl(PropertiesComponent propertiesComponent) {

        this.propertiesComponent = propertiesComponent;
    }

    @Override
    public void saveToPermanentStorage(ValuesRepositoryReader savedValues) {
        propertiesComponent.setValue(PropertiesFields.PLUGIN_ENABLED, String.valueOf(savedValues.isPluginEnabled()));

        propertiesComponent.setValue(PropertiesFields.HIGHLIGHT_LAYER_SELECTION_GROUP_0, String.valueOf(savedValues.getHighlightLayerOfSelectionGroup(0)));
        propertiesComponent.setValue(PropertiesFields.HIGHLIGHT_LAYER_SELECTION_GROUP_1, String.valueOf(savedValues.getHighlightLayerOfSelectionGroup(1)));
        propertiesComponent.setValue(PropertiesFields.HIGHLIGHT_LAYER_SELECTION_GROUP_2, String.valueOf(savedValues.getHighlightLayerOfSelectionGroup(2)));
        propertiesComponent.setValue(PropertiesFields.HIGHLIGHT_LAYER_SELECTION_GROUP_3, String.valueOf(savedValues.getHighlightLayerOfSelectionGroup(3)));
        propertiesComponent.setValue(PropertiesFields.HIGHLIGHT_LAYER_SELECTION_GROUP_4, String.valueOf(savedValues.getHighlightLayerOfSelectionGroup(4)));
        propertiesComponent.setValue(PropertiesFields.HIGHLIGHT_LAYER_SELECTION_GROUP_5, String.valueOf(savedValues.getHighlightLayerOfSelectionGroup(5)));
        propertiesComponent.setValue(PropertiesFields.HIGHLIGHT_LAYER_SELECTION_GROUP_6, String.valueOf(savedValues.getHighlightLayerOfSelectionGroup(6)));
        propertiesComponent.setValue(PropertiesFields.HIGHLIGHT_LAYER_SELECTION_GROUP_7, String.valueOf(savedValues.getHighlightLayerOfSelectionGroup(7)));

        propertiesComponent.setValue(PropertiesFields.MARKER_SELECTION_GROUP_0, String.valueOf(savedValues.isMarkerEnabledForSelectionGroup(0)));
        propertiesComponent.setValue(PropertiesFields.MARKER_SELECTION_GROUP_1, String.valueOf(savedValues.isMarkerEnabledForSelectionGroup(1)));
        propertiesComponent.setValue(PropertiesFields.MARKER_SELECTION_GROUP_2, String.valueOf(savedValues.isMarkerEnabledForSelectionGroup(2)));
        propertiesComponent.setValue(PropertiesFields.MARKER_SELECTION_GROUP_3, String.valueOf(savedValues.isMarkerEnabledForSelectionGroup(3)));
        propertiesComponent.setValue(PropertiesFields.MARKER_SELECTION_GROUP_4, String.valueOf(savedValues.isMarkerEnabledForSelectionGroup(4)));
        propertiesComponent.setValue(PropertiesFields.MARKER_SELECTION_GROUP_5, String.valueOf(savedValues.isMarkerEnabledForSelectionGroup(5)));
        propertiesComponent.setValue(PropertiesFields.MARKER_SELECTION_GROUP_6, String.valueOf(savedValues.isMarkerEnabledForSelectionGroup(6)));
        propertiesComponent.setValue(PropertiesFields.MARKER_SELECTION_GROUP_7, String.valueOf(savedValues.isMarkerEnabledForSelectionGroup(7)));

        propertiesComponent.setValue(PropertiesFields.COLOR_SELECTION_GROUP_0, generateRgbHexString(savedValues.getColorOfSelectionGroup(0)));
        propertiesComponent.setValue(PropertiesFields.COLOR_SELECTION_GROUP_1, generateRgbHexString(savedValues.getColorOfSelectionGroup(1)));
        propertiesComponent.setValue(PropertiesFields.COLOR_SELECTION_GROUP_2, generateRgbHexString(savedValues.getColorOfSelectionGroup(2)));
        propertiesComponent.setValue(PropertiesFields.COLOR_SELECTION_GROUP_3, generateRgbHexString(savedValues.getColorOfSelectionGroup(3)));
        propertiesComponent.setValue(PropertiesFields.COLOR_SELECTION_GROUP_4, generateRgbHexString(savedValues.getColorOfSelectionGroup(4)));
        propertiesComponent.setValue(PropertiesFields.COLOR_SELECTION_GROUP_5, generateRgbHexString(savedValues.getColorOfSelectionGroup(5)));
        propertiesComponent.setValue(PropertiesFields.COLOR_SELECTION_GROUP_6, generateRgbHexString(savedValues.getColorOfSelectionGroup(6)));
        propertiesComponent.setValue(PropertiesFields.COLOR_SELECTION_GROUP_7, generateRgbHexString(savedValues.getColorOfSelectionGroup(7)));
    }

    private String generateRgbHexString(Color colorOfSelectionGroup) {
        final int rgb = colorOfSelectionGroup.getRGB();
        final String hexValue = Integer.toHexString(rgb);
        return "0x" + hexValue.substring(2);
    }
}

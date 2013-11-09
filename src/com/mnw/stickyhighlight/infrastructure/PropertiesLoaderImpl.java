package com.mnw.stickyhighlight.infrastructure;

import com.intellij.ide.util.PropertiesComponent;
import com.mnw.stickyhighlight.model.DefaultValues;
import com.mnw.stickyhighlight.model.PropertiesLoader;
import com.mnw.stickyhighlight.model.ValuesRepository;
import com.mnw.stickyhighlight.model.ValuesRepositoryReader;

import java.awt.*;

/**
 * TODO description of this class is missing
 */
public class PropertiesLoaderImpl implements PropertiesLoader {
    private final PropertiesComponent propertiesComponent;

    public PropertiesLoaderImpl(PropertiesComponent propertiesComponent) {

        this.propertiesComponent = propertiesComponent;
    }

    @Override
    public ValuesRepositoryReader loadFromPermanentStorageOrDefault(final DefaultValues defaultValues) {

        final ValuesRepository ret = new ValuesRepository();

        ret.setIsPluginEnabled(propertiesComponent.getBoolean(PropertiesFields.PLUGIN_ENABLED, defaultValues.isPluginEnabled()));

        ret.setHighlightLayerOfSelectionGroup(0, getHighlightLayer(0, PropertiesFields.HIGHLIGHT_LAYER_SELECTION_GROUP_0, defaultValues));
        ret.setHighlightLayerOfSelectionGroup(1, getHighlightLayer(1, PropertiesFields.HIGHLIGHT_LAYER_SELECTION_GROUP_1, defaultValues));
        ret.setHighlightLayerOfSelectionGroup(2, getHighlightLayer(2, PropertiesFields.HIGHLIGHT_LAYER_SELECTION_GROUP_2, defaultValues));
        ret.setHighlightLayerOfSelectionGroup(3, getHighlightLayer(3, PropertiesFields.HIGHLIGHT_LAYER_SELECTION_GROUP_3, defaultValues));
        ret.setHighlightLayerOfSelectionGroup(4, getHighlightLayer(4, PropertiesFields.HIGHLIGHT_LAYER_SELECTION_GROUP_4, defaultValues));
        ret.setHighlightLayerOfSelectionGroup(5, getHighlightLayer(5, PropertiesFields.HIGHLIGHT_LAYER_SELECTION_GROUP_5, defaultValues));
        ret.setHighlightLayerOfSelectionGroup(6, getHighlightLayer(6, PropertiesFields.HIGHLIGHT_LAYER_SELECTION_GROUP_6, defaultValues));
        ret.setHighlightLayerOfSelectionGroup(7, getHighlightLayer(7, PropertiesFields.HIGHLIGHT_LAYER_SELECTION_GROUP_7, defaultValues));

        ret.setIsMarkerEnabledForSelectionGroup(0, propertiesComponent.getBoolean(PropertiesFields.MARKER_SELECTION_GROUP_0,
                                                                               defaultValues.isMarkerEnabledForSelectionGroup(0)));
        ret.setIsMarkerEnabledForSelectionGroup(1, propertiesComponent.getBoolean(PropertiesFields.MARKER_SELECTION_GROUP_1,
                                                                               defaultValues.isMarkerEnabledForSelectionGroup(1)));
        ret.setIsMarkerEnabledForSelectionGroup(2, propertiesComponent.getBoolean(PropertiesFields.MARKER_SELECTION_GROUP_2,
                                                                               defaultValues.isMarkerEnabledForSelectionGroup(2)));
        ret.setIsMarkerEnabledForSelectionGroup(3, propertiesComponent.getBoolean(PropertiesFields.MARKER_SELECTION_GROUP_3,
                                                                               defaultValues.isMarkerEnabledForSelectionGroup(3)));
        ret.setIsMarkerEnabledForSelectionGroup(4, propertiesComponent.getBoolean(PropertiesFields.MARKER_SELECTION_GROUP_4,
                                                                               defaultValues.isMarkerEnabledForSelectionGroup(4)));
        ret.setIsMarkerEnabledForSelectionGroup(5, propertiesComponent.getBoolean(PropertiesFields.MARKER_SELECTION_GROUP_5,
                                                                               defaultValues.isMarkerEnabledForSelectionGroup(5)));
        ret.setIsMarkerEnabledForSelectionGroup(6, propertiesComponent.getBoolean(PropertiesFields.MARKER_SELECTION_GROUP_6,
                                                                               defaultValues.isMarkerEnabledForSelectionGroup(6)));
        ret.setIsMarkerEnabledForSelectionGroup(7, propertiesComponent.getBoolean(PropertiesFields.MARKER_SELECTION_GROUP_7,
                                                                               defaultValues.isMarkerEnabledForSelectionGroup(7)));

        ret.setColorOfSelectionGroup(0, getColor(0, PropertiesFields.COLOR_SELECTION_GROUP_0, defaultValues));
        ret.setColorOfSelectionGroup(1, getColor(1, PropertiesFields.COLOR_SELECTION_GROUP_1, defaultValues));
        ret.setColorOfSelectionGroup(2, getColor(2, PropertiesFields.COLOR_SELECTION_GROUP_2, defaultValues));
        ret.setColorOfSelectionGroup(3, getColor(3, PropertiesFields.COLOR_SELECTION_GROUP_3, defaultValues));
        ret.setColorOfSelectionGroup(4, getColor(4, PropertiesFields.COLOR_SELECTION_GROUP_4, defaultValues));
        ret.setColorOfSelectionGroup(5, getColor(5, PropertiesFields.COLOR_SELECTION_GROUP_5, defaultValues));
        ret.setColorOfSelectionGroup(6, getColor(6, PropertiesFields.COLOR_SELECTION_GROUP_6, defaultValues));
        ret.setColorOfSelectionGroup(7, getColor(7, PropertiesFields.COLOR_SELECTION_GROUP_7, defaultValues));

        return ret;
    }

    private Color getColor(int groupNumber, String fieldName, DefaultValues defaultValues) {
        final Color colorOfSelectionGroup = defaultValues.getColorOfSelectionGroup(groupNumber);
        final String rgbHexString = Integer.toHexString(colorOfSelectionGroup.getRGB());
        final String defaultAsString = "0x" + rgbHexString.substring(2, rgbHexString.length());
//        final String defaultAsString = colorOfSelectionGroup..toString();
        final String value = propertiesComponent.getValue(fieldName, defaultAsString);
        return Color.decode(value);
    }

    private int getHighlightLayer(int groupNumber, String fieldName, DefaultValues defaultValues) {
        final String defaultAsString = String.valueOf(defaultValues.getHighlightLayerOfSelectionGroup(groupNumber));
        final String value = propertiesComponent.getValue(fieldName, defaultAsString);
        return Integer.parseInt(value);
    }

}

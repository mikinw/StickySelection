package com.mnw.stickyselection.infrastructure;

import com.mnw.stickyselection.model.DefaultValues;

import java.awt.Color;

public class PropertiesLoaderImpl {

    private Color getColor(int groupNumber, String fieldName, DefaultValues defaultValues) {
        final Color colorOfSelectionGroup = defaultValues.getColorOfSelectionGroup(groupNumber);
        final String rgbHexString = Integer.toHexString(colorOfSelectionGroup.getRGB());
        final String defaultAsString = "0x" + rgbHexString.substring(2, rgbHexString.length());
//        final String defaultAsString = colorOfSelectionGroup..toString();
//        final String value = propertiesComponent.getValue(fieldName, defaultAsString);
        final String value = defaultAsString;
        return Color.decode(value);
    }


}

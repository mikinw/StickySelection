package com.mnw.stickyselection.infrastructure;

import java.awt.Color;

public class PropertiesSaverImpl {

    private String generateRgbHexString(Color colorOfSelectionGroup) {
        final int rgb = colorOfSelectionGroup.getRGB();
        final String hexValue = Integer.toHexString(rgb);
        return "0x" + hexValue.substring(2);
    }
}

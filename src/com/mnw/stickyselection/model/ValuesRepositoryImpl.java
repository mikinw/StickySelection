package com.mnw.stickyselection.model;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.*;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;

@State(
        name = "StickySelectionProperties", storages = {
        @Storage(
                id = "other",
                file = "$APP_CONFIG$/StickySelection.xml")
})
public class ValuesRepositoryImpl implements ValuesRepository, PersistentStateComponent<ValuesRepositoryImpl> {

    @com.intellij.util.xmlb.annotations.AbstractCollection
    private java.util.List<PaintGroupDataBean> paintGroupProperties = new ArrayList<>();

    public String asdfghjk = "asdfghjkl";

    //private final int NUMBER_OF_SELECTION_GROUPS = 8;

    private boolean isPluginEnabled;
    //private boolean[] markerEnabled;
    //private int[] highlightLayer;
    //private Color[] color;

    public ValuesRepositoryImpl() {
        //markerEnabled = new boolean[NUMBER_OF_SELECTION_GROUPS];
        //highlightLayer = new int[NUMBER_OF_SELECTION_GROUPS];
        //color = new Color[NUMBER_OF_SELECTION_GROUPS];
    }

    /*public void setColorOfSelectionGroup(int groupNumber, Color color) {
        this.color[groupNumber] = color;
    }

    public void setHighlightLayerOfSelectionGroup(int groupNumber, int highlightLayer) {
        this.highlightLayer[groupNumber] = highlightLayer;
    }

    public void setIsMarkerEnabledForSelectionGroup(int groupNumber, boolean markerEnabled) {
        this.markerEnabled[groupNumber] = markerEnabled;
    }
*/
    @Override
    public void setIsPluginEnabled(boolean enabled) {
        isPluginEnabled = enabled;
    }

    @Override
    @Transient
    public PaintGroupDataBean getPaintGroupProperties(final int groupNumber) {
        return paintGroupProperties.get(groupNumber);
    }

    /*@Override
    public Color getColorOfSelectionGroup(int groupNumber) {
        return color[groupNumber];
    }

    @Override
    public int getHighlightLayerOfSelectionGroup(int groupNumber) {
        return highlightLayer[groupNumber];
    }

    @Override
    public boolean isMarkerEnabledForSelectionGroup(int groupNumber) {
        return markerEnabled[groupNumber];
    }*/



    @Override
    public boolean getIsPluginEnabled() {
        return isPluginEnabled;
    }

    @Override
    @Transient
    public int getPaintGroupCount() {
        return paintGroupProperties.size();
    }

    @Override
    @Transient
    public void addNewPaintGroup() {
        paintGroupProperties.add(new PaintGroupDataBean());
    }

    @Override
    @Transient
    public void removeFrom(int groupNumber) {
        while (paintGroupProperties.size() >= groupNumber - 1) {
            paintGroupProperties.remove(paintGroupProperties.size() - 1);
        }
    }

    @Nullable
    @Override
    public ValuesRepositoryImpl getState() {
        return this;
    }

    @Override
    public void loadState(ValuesRepositoryImpl state) {
        XmlSerializerUtil.copyBean(state, this);
        for (PaintGroupDataBean paintGroupProperty : paintGroupProperties) {
            if (paintGroupProperty.getColor() == null) {
                paintGroupProperty.setColor(Color.CYAN);
            }
        }

    }
}

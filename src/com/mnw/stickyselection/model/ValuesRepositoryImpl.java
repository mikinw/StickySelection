package com.mnw.stickyselection.model;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.*;
import com.mnw.stickyselection.infrastructure.RandomPaintGroupData;
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

    private int idStore = 1;

    private boolean isPluginEnabled;

    public ValuesRepositoryImpl() {
    }

    @Override
    public void setIsPluginEnabled(boolean enabled) {
        isPluginEnabled = enabled;
    }

    @Override
    @Transient
    public PaintGroupDataBean getPaintGroupProperties(final int groupNumber) {
        return paintGroupProperties.get(groupNumber);
    }

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
        final PaintGroupDataBean bean = RandomPaintGroupData.createBean();
        bean.setId(idStore++);
        paintGroupProperties.add(bean);
    }

    @Override
    @Transient
    public void removeWithIds(final List<Integer> idsToRemove) {
        final Iterator<PaintGroupDataBean> iterator = paintGroupProperties.iterator();
        while (iterator.hasNext()) {
            final PaintGroupDataBean next = iterator.next();
            final int indexInRemoveList = idsToRemove.indexOf(next.getId());
            if (indexInRemoveList >= 0) {
                idsToRemove.remove(indexInRemoveList);
                iterator.remove();
            }
        }
    }

    @Override
    @Transient
    public boolean hasDataBeanId(int dataBeanId) {
        for (PaintGroupDataBean paintGroupProperty : paintGroupProperties) {
            if (paintGroupProperty.getId() == dataBeanId) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transient
    public PaintGroupDataBean getPaintGroupPropertiesWithId(int dataBeanId) {
        for (PaintGroupDataBean paintGroupProperty : paintGroupProperties) {
            if (paintGroupProperty.getId() == dataBeanId) {
                return paintGroupProperty;
            }
        }
        return null;
    }

    @Override
    @Transient
    public PaintGroupDataBean getLast() {
        return paintGroupProperties.get(paintGroupProperties.size() - 1);

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
            paintGroupProperty.setId(idStore++);
        }

    }
}

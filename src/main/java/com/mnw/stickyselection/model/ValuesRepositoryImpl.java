package com.mnw.stickyselection.model;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.ui.JBColor;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import com.mnw.stickyselection.infrastructure.RandomPaintGroupData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@State(
        name = "com.mnw.stickyselection.StickySelectionProperties",
        storages = {@Storage("StickySelection.xml")}
)
public class ValuesRepositoryImpl implements ValuesRepository, PersistentStateComponent<ValuesRepositoryImpl> {

    @com.intellij.util.xmlb.annotations.XCollection
    private final java.util.List<PaintGroupDataBean> paintGroupProperties = new ArrayList<>();

    private int idStore = 1;

    private boolean isPluginEnabled;
    private boolean isCycleThroughEnabled = true;
    private boolean isPersistHighlights = true;

    public ValuesRepositoryImpl() {
    }

    public static ValuesRepository getInstance() {
        return ApplicationManager.getApplication().getService(ValuesRepository.class);
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
    public PaintGroupDataBean addNewPaintGroup() {
        final PaintGroupDataBean bean = RandomPaintGroupData.createBean();
        bean.setId(idStore++);
        paintGroupProperties.add(bean);
        return bean;
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
    public List<Integer> getPaintGroupIds() {
        final Iterator<PaintGroupDataBean> iterator = paintGroupProperties.iterator();
        final List<Integer> idList = new ArrayList<>();

        while (iterator.hasNext()) {
            final PaintGroupDataBean next = iterator.next();
            idList.add(next.getId());
        }

        return idList;
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

    @Override
    public boolean getIsCycleThroughEnabled() {
        return isCycleThroughEnabled;
    }

    @Override
    public void setIsCycleThroughEnabled(boolean isCycleThroughEnabled) {
        this.isCycleThroughEnabled = isCycleThroughEnabled;
    }

    @Override
    public boolean getPersistHighlights() {
        return isPersistHighlights;
    }

    @Override
    public void setPersistHighlights(boolean persistHighlights) {
        isPersistHighlights = persistHighlights;
    }

    @Nullable
    @Override
    public ValuesRepositoryImpl getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ValuesRepositoryImpl state) {
        XmlSerializerUtil.copyBean(state, this);
        for (PaintGroupDataBean paintGroupProperty : paintGroupProperties) {
            if (paintGroupProperty.getColor() == null) {
                paintGroupProperty.setColor(JBColor.CYAN);
            }
            paintGroupProperty.setId(idStore++);
        }

    }
}

package com.mnw.stickyselection.model;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

import java.util.List;


public interface ValuesRepository {

    PaintGroupDataBean getPaintGroupProperties(int groupNumber);
    void setIsPluginEnabled(boolean enabled);
    boolean getIsPluginEnabled();
    int getPaintGroupCount();
    PaintGroupDataBean addNewPaintGroup();
    void removeWithIds(List<Integer> idsToRemove);

    List<Integer> getPaintGroupIds();
    boolean hasDataBeanId(int dataBeanId);
    PaintGroupDataBean getPaintGroupPropertiesWithId(int dataBeanId);
    PaintGroupDataBean getLast();
    boolean getIsCycleThroughEnabled();
    void setIsCycleThroughEnabled(boolean isCycleThroughEnabled);
    boolean getPersistHighlights();
    void setPersistHighlights(boolean persistHighlights);
}

package com.mnw.stickyselection.model;

import java.util.List;

public interface ValuesRepository {

    PaintGroupDataBean getPaintGroupProperties(int groupNumber);
    void setIsPluginEnabled(boolean enabled);
    boolean getIsPluginEnabled();
    int getPaintGroupCount();
    void addNewPaintGroup();
    void removeWithIds(List<Integer> idsToRemove);
    boolean hasDataBeanId(int dataBeanId);
    PaintGroupDataBean getPaintGroupPropertiesWithId(int dataBeanId);
    PaintGroupDataBean getLast();
    boolean getIsCycleThroughEnabled();
    void setIsCycleThroughEnabled(boolean isCycleThroughEnabled);
}

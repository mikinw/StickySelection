package com.mnw.stickyselection.model;

public interface ValuesRepository {

    PaintGroupDataBean getPaintGroupProperties(int groupNumber);
    void setIsPluginEnabled(boolean enabled);
    boolean getIsPluginEnabled();
    int getPaintGroupCount();
    void addNewPaintGroup();
    void removeFrom(int groupNumber);
}

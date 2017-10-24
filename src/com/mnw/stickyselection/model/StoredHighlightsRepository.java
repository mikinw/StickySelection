package com.mnw.stickyselection.model;

import com.intellij.openapi.components.*;
import com.intellij.util.containers.HashMap;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@State(
        name = "StickySelectionHighlights", storages = {
        @Storage(
            id = "dir",
            file = "$PROJECT_CONFIG_DIR$/StickySelectionHighlights.xml",
            scheme = StorageScheme.DIRECTORY_BASED
        )
})
public class StoredHighlightsRepository implements PersistentStateComponent<StoredHighlightsRepository> {

    public Map<String, PaintGroupHighlightMap> editorHighlights = new HashMap<>();

    @Nullable
    @Override
    @Transient
    public StoredHighlightsRepository getState() {
        return this;
    }

    @Override
    @Transient
    public void loadState(StoredHighlightsRepository state) {
        XmlSerializerUtil.copyBean(state, this);

    }

    @Transient
    public Map<Integer, EditorHighlightsForPaintGroup> getEditorHighlights(String relativePath) {
        final Map<Integer, EditorHighlightsForPaintGroup> editorHighlightsOfAllPaintGroups = editorHighlights.get(relativePath);
        return editorHighlightsOfAllPaintGroups != null ? editorHighlightsOfAllPaintGroups : new HashMap<>();
    }

    @Transient
    public void addOrUpdateEditorHighlights(PaintGroupHighlightMap editorHighlightsOfAllPaintGroups, String relativePath) {
        editorHighlights.remove(relativePath);
        editorHighlights.put(relativePath, editorHighlightsOfAllPaintGroups);
    }

    @Transient
    public void addOneHighlight(String filePath, int paintGroup, int startOffset, int endOffset) {
        if (!ServiceManager.getService(ValuesRepository.class).getPersistHighlights()) {
            return;
        }
        if (!editorHighlights.containsKey(filePath)) {
            editorHighlights.put(filePath, new PaintGroupHighlightMap());
        }
        final Map<Integer, EditorHighlightsForPaintGroup> paintGroupMap = editorHighlights.get(filePath);
        if (!paintGroupMap.containsKey(paintGroup)) {
            paintGroupMap.put(paintGroup, new EditorHighlightsForPaintGroup());
        }
        final EditorHighlightsForPaintGroup highlightOffsets = paintGroupMap.get(paintGroup);
        highlightOffsets.add(new HighlightOffset(startOffset, endOffset));
    }

    @Transient
    public void removeHighlightsOfPaintGroup(String filePath, int paintGroup) {
        if (!ServiceManager.getService(ValuesRepository.class).getPersistHighlights()) {
            return;
        }
        if (!editorHighlights.containsKey(filePath)) {
            return;
        }
        final Map<Integer, EditorHighlightsForPaintGroup> paintGroupMap = editorHighlights.get(filePath);
        if (!paintGroupMap.containsKey(paintGroup)) {
            return;
        }

        final EditorHighlightsForPaintGroup highlightOffsets = paintGroupMap.get(paintGroup);
        highlightOffsets.clear();

        paintGroupMap.remove(paintGroup);
        if (paintGroupMap.isEmpty()) {
            editorHighlights.remove(filePath);
        }
    }

    @Transient
    public void removeLastNOfPaintGroup(String filePath, int paintGroup, int lastN) {
        if (!ServiceManager.getService(ValuesRepository.class).getPersistHighlights()) {
            return;
        }
        if (!editorHighlights.containsKey(filePath)) {
            return;
        }
        final Map<Integer, EditorHighlightsForPaintGroup> paintGroupMap = editorHighlights.get(filePath);
        if (!paintGroupMap.containsKey(paintGroup)) {
            return;
        }

        final EditorHighlightsForPaintGroup highlightOffsets = paintGroupMap.get(paintGroup);
        final int clearUpUntil = highlightOffsets.size() - lastN;
        for (int i = highlightOffsets.size() - 1; i >= clearUpUntil; i--) {
            highlightOffsets.remove(i);
        }

        paintGroupMap.remove(paintGroup);
        if (paintGroupMap.isEmpty()) {
            editorHighlights.remove(filePath);
        }

    }

    @Transient
    public void clear() {
        editorHighlights.clear();

    }
}

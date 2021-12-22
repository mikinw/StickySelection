package com.mnw.stickyselection.model;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@State(
        name = "StickySelectionHighlights", storages = {
        @Storage("StickySelectionHighlights.xml")
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
    public void loadState(@NotNull StoredHighlightsRepository state) {
        XmlSerializerUtil.copyBean(state, this);

    }

    @Transient
    public Map<Integer, EditorHighlightsForPaintGroup> getEditorHighlights(String relativePath) {
        final Map<Integer, EditorHighlightsForPaintGroup> editorHighlightsOfAllPaintGroups = editorHighlights.get(relativePath);
        return editorHighlightsOfAllPaintGroups != null ? editorHighlightsOfAllPaintGroups : new HashMap<Integer, EditorHighlightsForPaintGroup>();
    }

    @Transient
    public void addOrUpdateEditorHighlights(PaintGroupHighlightMap editorHighlightsOfAllPaintGroups, String relativePath) {
        editorHighlights.remove(relativePath);

        final PaintGroupHighlightMap trimmedCopy = new PaintGroupHighlightMap();
        editorHighlightsOfAllPaintGroups.getPaintGroups().forEach((index, paintGroup) -> {
            if (!paintGroup.isEmpty()) {
                trimmedCopy.put(index, paintGroup);
            }
        });

        if (!trimmedCopy.isEmpty()) {
            editorHighlights.put(relativePath, trimmedCopy);
        }
    }

    @Transient
    public void addOneHighlight(String filePath, int paintGroup, int startOffset, int endOffset) {
        if (!ValuesRepositoryImpl.getInstance().getPersistHighlights()) {
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
        if (!ValuesRepositoryImpl.getInstance().getPersistHighlights()) {
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
        if (!ValuesRepositoryImpl.getInstance().getPersistHighlights()) {
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

package com.mnw.stickyselection;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.MarkupModelEx;
import com.intellij.openapi.editor.ex.RangeHighlighterEx;
import com.intellij.openapi.editor.impl.event.MarkupModelListener;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.mnw.stickyselection.actions.UndoLastPaintAction;
import com.mnw.stickyselection.infrastructure.FindClosestHighlighter;
import com.mnw.stickyselection.infrastructure.SuggestCaret;
import com.mnw.stickyselection.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class StickySelectionEditorComponent implements Disposable {

    private static final boolean REMOVE_ALL_CARETS = true;
    private final Editor editor;
    private final String filePath;
    private final Project project;
    private final List<PaintGroup> paintGroups = new ArrayList<>();

    private final List<RangeHighlighter> undoList = new ArrayList<>();

    private final Map<RangeHighlighter, Integer> highlighterPaintGroupMap = new HashMap<>();
    private int lastPaintedGroup;

    private int navigationGroup = -1;
    private boolean documentModified = false;


    /*package*/ StickySelectionEditorComponent(Editor editor) {
        this.editor = editor;

        final VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
        filePath = file != null ? file.getPath() : null;
        project = editor.getProject();


        ValuesRepository savedValues = ValuesRepositoryImpl.getInstance();

        for (int i = 0; i < savedValues.getPaintGroupCount(); i++) {
            paintGroups.add(new PaintGroup(savedValues.getPaintGroupProperties(i)));
        }

        editor.getDocument().addDocumentListener(new ClearUndoFieldsWhenChanged());
        editor.getDocument().addDocumentListener(new SetModifiedFlag());
        editor.getCaretModel().addCaretListener(new CaretListener() {
            @Override
            public void caretPositionChanged(CaretEvent caretEvent) {
                navigationGroup = -1;
            }

            @Override
            public void caretAdded(CaretEvent caretEvent) { }

            @Override
            public void caretRemoved(CaretEvent caretEvent) { }

        });
        ((MarkupModelEx) editor.getMarkupModel()).addMarkupModelListener(this, new MarkupModelListener() {
            @Override
            public void afterAdded(@NotNull RangeHighlighterEx rangeHighlighterEx) { }

            @Override
            public void beforeRemoved(@NotNull RangeHighlighterEx rangeHighlighterEx) {
                final Integer paintGroupIndex = highlighterPaintGroupMap.remove(rangeHighlighterEx);
                if (paintGroupIndex == null) {return;}
                final PaintGroup paintGroup = paintGroups.get(paintGroupIndex);
                if (paintGroup == null) {return;}
                paintGroup.remove(rangeHighlighterEx);
            }

        });

    }

    @Override
    public void dispose() {
        clearState();
//        editor = null;
    }

    protected void clearState() {
        final MarkupModel markupModel = editor.getMarkupModel();
        for (PaintGroup paintGroup : paintGroups) {
            paintGroup.clear(markupModel);
        }
    }


    public void paintSelection(int paintGroup) {
        PaintGroupDataBean paintGroupProperties = ValuesRepositoryImpl.getInstance().getPaintGroupProperties(paintGroup);

        if (editor.getCaretModel().getCaretCount() > 1) {
            final List<CaretState> caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();
            final TextAttributes textAttributes = createTextAttributes(paintGroupProperties);

            clearUndoFields();
            lastPaintedGroup = paintGroup;

            for (CaretState caretsAndSelection : caretsAndSelections) {
                final LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
                final LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
                if (selectionStart == null || selectionEnd == null) {
                    continue;
                }
                final int selectionStartOffset = editor.logicalPositionToOffset(selectionStart);
                final int selectionEndOffset = editor.logicalPositionToOffset(selectionEnd);
                if (selectionStartOffset == selectionEndOffset) {
                    continue;
                }
                addRangeHighlighter(paintGroup, paintGroupProperties, textAttributes, selectionStartOffset, selectionEndOffset, true);

            }
        } else {
            if (StringUtil.isEmpty(editor.getSelectionModel().getSelectedText())) {
                editor.getSelectionModel().selectWordAtCaret(true);
            }
            final String selectedText = editor.getSelectionModel().getSelectedText();
            if (StringUtil.isEmpty(selectedText)) {
                return;
            }


            clearUndoFields();
            lastPaintedGroup = paintGroup;

            final int lengthOfSelection = selectedText.length();
            final String wholeText = editor.getDocument().getText();

            ArrayList<Integer> selectedMatchStart = getSelectionMatchesStart(wholeText, selectedText);

            final TextAttributes textAttributes = createTextAttributes(paintGroupProperties);
            for (Integer index : selectedMatchStart) {
                addRangeHighlighter(paintGroup, paintGroupProperties, textAttributes, index, index + lengthOfSelection, true);
            }
        }

        editor.getSelectionModel().removeSelection(REMOVE_ALL_CARETS);
        setUndoEnabled(!undoList.isEmpty());
        paintGroups.get(paintGroup).highlighters.sort(new Comparator<RangeHighlighter>() {
            @Override
            public int compare(RangeHighlighter o1, RangeHighlighter o2) {
                return ((Integer)o1.getStartOffset()).compareTo(o2.getStartOffset());
            }
        });

        //paintGroups.get(paintGroup).highlighters.sort((o1, o2)-> ((Integer)o1.getStartOffset()).compareTo(o2.getStartOffset()));

    }

    @NotNull
    private TextAttributes createTextAttributes(PaintGroupDataBean paintGroupProperties) {
        final TextAttributes textAttributes = new TextAttributes();
        textAttributes.setBackgroundColor(paintGroupProperties.getColor());
        if (paintGroupProperties.isFrameNeeded()) {
            textAttributes.setEffectType(EffectType.BOXED);
            textAttributes.setEffectColor(paintGroupProperties.getColor());
        }
        return textAttributes;
    }

    private void addRangeHighlighter(int paintGroup, PaintGroupDataBean paintGroupProperties,
                                     TextAttributes textAttributes, int startOffset, int endOffset, boolean persist) {
        if (startOffset < 0 || endOffset < 0 || endOffset <= startOffset) {
            // don't try to add invalid range
            return;
        }

        final RangeHighlighter rangeHighlighter = editor.getMarkupModel().addRangeHighlighter(
                startOffset,
                endOffset,
                paintGroupProperties.getHighlightLayer(),
                textAttributes,
                HighlighterTargetArea.EXACT_RANGE);
        if (paintGroupProperties.isMarkerNeeded()) {
            rangeHighlighter.setErrorStripeMarkColor(paintGroupProperties.getColor());
        }

        // TODO: 2017. 10. 17. don't add the same (start, end, paintgroup) twice

        //if (paintGroups[paintGroup].has(rangeHighlighter)) {
        //    editor.getMarkupModel().removeHighlighter(rangeHighlighter);
        //} else {

        undoList.add(rangeHighlighter);
        paintGroups.get(paintGroup).add(rangeHighlighter);
        highlighterPaintGroupMap.put(rangeHighlighter, paintGroup);

        if (persist) {
            final StoredHighlightsRepository projectSettings = project.getService(StoredHighlightsRepository.class);
            projectSettings.addOneHighlight(filePath,
                    paintGroup,
                    rangeHighlighter.getStartOffset(),
                    rangeHighlighter.getEndOffset());
        }
    }

    private void setUndoEnabled(boolean enabled) {
        DefaultActionGroup editorMenu = (DefaultActionGroup) ActionManager.getInstance().getAction("StickyHighlight.Menu");
        final AnAction[] childActionsOrStubs = editorMenu.getChildActionsOrStubs();
        for(int i = 0; i < childActionsOrStubs.length; i++) {
            AnAction childActionsOrStub = childActionsOrStubs[i];

            if (childActionsOrStub instanceof UndoLastPaintAction) {
                childActionsOrStub.getTemplatePresentation().setEnabled(enabled);
            }
        }
    }

    private ArrayList<Integer> getSelectionMatchesStart(String wholeText, String selectedText) {
        ArrayList<Integer> selectedMatchStart = new ArrayList<Integer>();

        int index;
        int offset = 0;
        //System.out.println(selectedText);
        while ((index = wholeText.indexOf(selectedText, offset)) > -1) {
//            System.out.println("select: index=" + index + " offset=" + offset);
            selectedMatchStart.add(index);
            offset = index + 1;
        }

        return selectedMatchStart;
    }

    public void loadHighlights(Map<Integer, EditorHighlightsForPaintGroup> editorHighlights) {

        final int documentLength = editor.getDocument().getTextLength();
        for (Integer paintGroup : editorHighlights.keySet()) {
            final List<HighlightOffset> highlightsForPaintGroup = editorHighlights.get(paintGroup);
            if (highlightsForPaintGroup == null) {
                continue;
            }

            final ValuesRepository valuesRepository = ValuesRepositoryImpl.getInstance();
            if (valuesRepository.getPaintGroupCount() - 1 < paintGroup) {
                final StoredHighlightsRepository projectSettings = project.getService(StoredHighlightsRepository.class);
                // TODO: 28/11/2017 ask the user if it should be removed (and only remove it outside of the loop)
                //projectSettings.removeHighlightsOfPaintGroup(filePath, paintGroup);
                continue;
            }
            PaintGroupDataBean paintGroupProperties = valuesRepository.getPaintGroupProperties(paintGroup);

            final TextAttributes textAttributes = createTextAttributes(paintGroupProperties);

            for (HighlightOffset editorHighlight : highlightsForPaintGroup) {
                if (editorHighlight.start > documentLength) {
                    continue;
                }
                addRangeHighlighter(
                        paintGroup,
                        paintGroupProperties,
                        textAttributes,
                        editorHighlight.start,
                        Math.min(editorHighlight.end, documentLength),
                        false
                );
            }
        }
        clearUndoFields();
    }


    public void clearPaintGroup(int paintGroup) {
        paintGroups.get(paintGroup).clear(editor.getMarkupModel());
        final StoredHighlightsRepository projectSettings = project.getService(StoredHighlightsRepository.class);
        projectSettings.removeHighlightsOfPaintGroup(filePath, paintGroup);

        if (paintGroup == lastPaintedGroup) {
            clearUndoFields();
        }
    }

    public void clearAll() {
        for (int i = 0; i < paintGroups.size(); i++) {
            clearPaintGroup(i);
        }

    }

    public void updateAllHighlighters() {
        final ValuesRepository repoValues = ValuesRepositoryImpl.getInstance();

        final int paintedGroupSize = paintGroups.size();

        if (paintedGroupSize < repoValues.getPaintGroupCount()) {
            for (int i = paintedGroupSize; i < repoValues.getPaintGroupCount(); i++) {
                paintGroups.add(new PaintGroup(repoValues.getPaintGroupProperties(i)));
            }
        }


        for (int i = 0; i < repoValues.getPaintGroupCount(); i++) {
            final PaintGroupDataBean paintGroupProperties = repoValues.getPaintGroupProperties(i);

            final PaintGroup paintGroup = paintGroups.get(i);
            if (!paintGroup.hasSameDataBean(paintGroupProperties)) {
                paintGroup.repaint(paintGroupProperties, editor.getMarkupModel());
            }
        }


        for (int i = paintedGroupSize - 1; i >= repoValues.getPaintGroupCount(); i--) {
            paintGroups.get(i).clear(editor.getMarkupModel());
            paintGroups.remove(i);
        }

    }

    public void undoLastPaint() {
        if (undoList.isEmpty()) {
            Notification notification = new Notification(
                    "StickySelection warnings",
                    "There is nothing to undo.",
                    "Maybe because the document has changed.",
                    NotificationType.INFORMATION);
            Notifications.Bus.notify(notification);
            setUndoEnabled(false);
            return;
        }
        for (RangeHighlighter rangeHighlighter : undoList) {
            paintGroups.get(lastPaintedGroup).remove(rangeHighlighter);
            editor.getMarkupModel().removeHighlighter(rangeHighlighter);
        }
        final StoredHighlightsRepository projectSettings = project.getService(StoredHighlightsRepository.class);
        projectSettings.removeLastNOfPaintGroup(filePath, lastPaintedGroup, undoList.size());
        clearUndoFields();
    }

    public void convertPaintGroupToSelection(int i) {
        final ArrayList<RangeHighlighter> highlighters = paintGroups.get(i).highlighters;
        if (highlighters.isEmpty()) {
            Notification notification = new Notification(
                    "StickySelection warnings",
                    "This Paint Group is currently empty",
                    "Nothing to convert",
                    NotificationType.INFORMATION);
            Notifications.Bus.notify(notification);
            //HintManager.getInstance().showInformationHint(editor, "This Paint Group is currently empty");
            return;
        }

        final CaretModel caretModel = editor.getCaretModel();
        if (caretModel.supportsMultipleCarets()) {
            List<CaretState> listOfCarets = new ArrayList<>();

            for (RangeHighlighter highlighter : highlighters) {
                final LogicalPosition startPosition = editor.offsetToLogicalPosition(highlighter.getStartOffset());
                final LogicalPosition endPosition = editor.offsetToLogicalPosition(highlighter.getEndOffset());

                listOfCarets.add(new CaretState(startPosition, startPosition, endPosition));
            }

            clearPaintGroup(i);

            caretModel.setCaretsAndSelections(listOfCarets);
        }


    }

    public void navigateToPaint(final SuggestCaret suggestCaret,
                                final FindClosestHighlighter findClosestHighlighter) {
        final boolean isCycleThroughEnabled = ValuesRepositoryImpl.getInstance().getIsCycleThroughEnabled();

        final CaretModel caretModel = editor.getCaretModel();

        final int currentCaret = caretModel.getOffset();

        // if there was a previous navigation action, try to stick to it, and navigate inside that group
        if (navigationGroup >= 0) {
            final ArrayList<RangeHighlighter> highlighters = paintGroups.get(navigationGroup).highlighters;
            final int suggestedCaretPos = suggestCaret.findCaretInPaintGroup(currentCaret, highlighters, isCycleThroughEnabled);
            if (moveCaret(navigationGroup, suggestedCaretPos)) {
                return;
            }
        }

        // find the first paint group that has the caret inside one of its paints
        final int length = paintGroups.size();
        for (int i1 = 0; i1 < length; i1++) {
            final ArrayList<RangeHighlighter> highlighters = paintGroups.get(i1).highlighters;
            final int suggestedCaretPos = suggestCaret.findCaretInPaintGroup(currentCaret, highlighters, isCycleThroughEnabled);
            if (moveCaret(i1, suggestedCaretPos)) {
                return;
            }
        }

        // find the closest paint group regardless of groups
        navigateToClosestPaint(findClosestHighlighter);
    }

    public void navigateToClosestPaint(final FindClosestHighlighter findClosestHighlighter) {
        final ValuesRepository savedValues = ValuesRepositoryImpl.getInstance();

        final int currentCaret = editor.getCaretModel().getOffset();
        CurrentBest currentBest = new CurrentBest(editor.getDocument().getTextLength(), currentCaret);
        int documentLength = editor.getDocument().getTextLength();
        final int length = paintGroups.size();
        for (int i = 0; i < length; i++) {
            final ArrayList<RangeHighlighter> highlighters = paintGroups.get(i).highlighters;

            findClosestHighlighter.findUpcoming(currentCaret, currentBest, i, highlighters);

            if (savedValues.getIsCycleThroughEnabled()) {
                findClosestHighlighter.findFromStart(currentCaret, documentLength, currentBest, i, highlighters);
            }
        }
        moveCaret(currentBest.paintGroup, currentBest.getCaretOffset());
    }


    private boolean moveCaret(int currentPaintGroup, int suggestedCaretPos) {
        if (suggestedCaretPos >= 0) {
            editor.getCaretModel().getPrimaryCaret().moveToOffset(suggestedCaretPos);
            navigationGroup = currentPaintGroup;
            editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
            return true;
        }
        return false;
    }

    private void clearUndoFields() {
        undoList.clear();
        lastPaintedGroup = -1;
        setUndoEnabled(false);

    }

    public PaintGroupHighlightMap highlightsToMap() {
        final PaintGroupHighlightMap ret = new PaintGroupHighlightMap();
        for (int paintGroup = 0; paintGroup < paintGroups.size(); paintGroup++) {
            EditorHighlightsForPaintGroup highlightsForPaintGroup = new EditorHighlightsForPaintGroup();

            final ArrayList<RangeHighlighter> highlighters = paintGroups.get(paintGroup).highlighters;
            for (RangeHighlighter highlighter : highlighters) {
                if (highlighter.getStartOffset() >= 0 && highlighter.getEndOffset() >= 0 && highlighter.getEndOffset() > highlighter.getStartOffset()) {
                    final HighlightOffset highlightOffset = new HighlightOffset(highlighter.getStartOffset(),
                            highlighter.getEndOffset());
                    highlightsForPaintGroup.add(highlightOffset);
                }
            }
            /*paintGroups.get(paintGroup).highlighters.addAll(
                    highlighters.stream()
                            .map(rangeHighlighter->new HighlightOffset(rangeHighlighter.getStartOffset(),
                                                                       rangeHighlighter.getEndOffset()))
                            .collect(Collectors.toList()));*/
            ret.put(paintGroup, highlightsForPaintGroup);
        }
        return ret;
    }

    public void persistHighlights(final boolean force) {
        if (documentModified || force) {
            final StoredHighlightsRepository projectSettings = project.getService(StoredHighlightsRepository.class);

            final VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
            if (file == null) {
                return;
            }
            final String path = file.getPath();

            projectSettings.addOrUpdateEditorHighlights(highlightsToMap(), path);

            documentModified = false;
        }

    }

    public void persistHighlights(Document document) {
        if (document == editor.getDocument()) {
            persistHighlights(false);
        }
    }

    public static class CurrentBest {
        private int currentClosestDistance;
        private int currentBestCaretOffset;

        private int paintGroup;

        public CurrentBest(final int currentClosestDistance, final int currentBestCaretOffset) {
            this.currentClosestDistance = currentClosestDistance;
            this.currentBestCaretOffset = currentBestCaretOffset;
        }

        public int getCaretOffset() {
            return currentBestCaretOffset;
        }

        public int getClosestDistance() {
            return currentClosestDistance;
        }

        public void setClosestDistance(final int closestDistance) {
            this.currentClosestDistance = closestDistance;
        }

        public void setCaretOffset(final int caretOffset) {
            this.currentBestCaretOffset = caretOffset;
        }

        public void setPaintGroup(final int paintGroup) {
            this.paintGroup = paintGroup;
        }

    }
    private class ClearUndoFieldsWhenChanged implements DocumentListener {

        @Override
        public void beforeDocumentChange(@NotNull DocumentEvent event) {}
        @Override
        public void documentChanged(@NotNull DocumentEvent event) {
            clearUndoFields();
        }

    }

    private class SetModifiedFlag implements DocumentListener {
        @Override
        public void beforeDocumentChange(@NotNull DocumentEvent event) {}

        @Override
        public void documentChanged(@NotNull DocumentEvent event) {
            documentModified = true;
        }
    }
}

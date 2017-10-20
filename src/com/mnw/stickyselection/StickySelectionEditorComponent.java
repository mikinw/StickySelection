package com.mnw.stickyselection;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionStub;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.ui.ex.MessagesEx;
import com.jgoodies.common.base.Strings;
import com.mnw.stickyselection.actions.UndoLastPaintAction;
import com.mnw.stickyselection.model.PaintGroupDataBean;
import com.mnw.stickyselection.model.ValuesRepository;

import java.util.*;

public class StickySelectionEditorComponent {

    private static final int MAX_NUMBER_OF_PAINT_GROUPS = 8;
    private static final int INITIAL_CAPACITY = 16;
    private Editor editor;
    protected List<PaintGroup> paintGroups = new ArrayList<>();

    private List<RangeHighlighter> undoList = new ArrayList<>();
    private int lastPaintedGroup;

    private int navigationGroup = -1;


    public StickySelectionEditorComponent(Editor editor) {
        this.editor = editor;
        ValuesRepository savedValues = ServiceManager.getService(ValuesRepository.class);

        for (int i = 0; i < savedValues.getPaintGroupCount(); i++) {
            paintGroups.add(new PaintGroup(savedValues.getPaintGroupProperties(i)));
        }

        editor.getDocument().addDocumentListener(new ClearUndoFieldsWhenChanged());
        editor.getCaretModel().addCaretListener(new CaretListener() {
            @Override
            public void caretPositionChanged(CaretEvent caretEvent) {
                navigationGroup = -1;
            }

            @Override
            public void caretAdded(CaretEvent caretEvent) {

            }

            @Override
            public void caretRemoved(CaretEvent caretEvent) {

            }
        });
        setUndoEnabled(false);

    }

    public void dispose() {
        clearState();
        editor = null;
    }

    protected void clearState() {
        final MarkupModel markupModel = editor.getMarkupModel();
        for (PaintGroup paintGroup : paintGroups) {
            paintGroup.clear(markupModel);
        }
    }


    public void paintSelection(int paintGroup) {
        PaintGroupDataBean paintGroupProperties = ServiceManager.getService(ValuesRepository.class).getPaintGroupProperties(paintGroup);
        if (Strings.isEmpty(editor.getSelectionModel().getSelectedText())) {
            editor.getSelectionModel().selectWordAtCaret(true);
        }
        final String selectedText = editor.getSelectionModel().getSelectedText();
        if (Strings.isEmpty(selectedText)) {
            return;
        }


        clearUndoFields();
        lastPaintedGroup = paintGroup;

        final int lengthOfSelection = selectedText.length();
        final String wholeText = editor.getDocument().getText();

        ArrayList<Integer> selectedMatchStart = getSelectionMatchesStart(wholeText, selectedText);

        final TextAttributes textAttributes = new TextAttributes();
        textAttributes.setBackgroundColor(paintGroupProperties.getColor());
        if (paintGroupProperties.isFrameNeeded()) {
            textAttributes.setEffectType(EffectType.BOXED);
            textAttributes.setEffectColor(paintGroupProperties.getColor());
        }
        for (Integer index : selectedMatchStart) {
            final RangeHighlighter rangeHighlighter = editor.getMarkupModel().addRangeHighlighter(
                    index,
                    index + lengthOfSelection,
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
        }

        setUndoEnabled(!undoList.isEmpty());
        paintGroups.get(paintGroup).highlighters.sort((o1, o2)-> ((Integer)o1.getStartOffset()).compareTo(o2.getStartOffset()));

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

    public void clearPaintGroup(int paintGroup) {
        paintGroups.get(paintGroup).clear(editor.getMarkupModel());
    }


    public void clearAll() {
        for (PaintGroup paintGroup : paintGroups) {
            paintGroup.clear(editor.getMarkupModel());
        }

    }

    public void updateAllHighlighters() {
        final ValuesRepository savedValues = ServiceManager.getService(ValuesRepository.class);

        final List<PaintGroupDataBean> newPaintGroupBeans = new ArrayList<>(savedValues.getPaintGroupCount());
        final List<Integer> missingPaintGroups = new ArrayList<>(paintGroups.size());
        for (int i = 0; i < paintGroups.size(); i++) {
            missingPaintGroups.add(i);
        }

        for (int i = 0; i < savedValues.getPaintGroupCount(); i++) {
            final PaintGroupDataBean paintGroupProperties = savedValues.getPaintGroupProperties(i);
            if (!repaintIfExisted(missingPaintGroups, paintGroupProperties)) {
                newPaintGroupBeans.add(paintGroupProperties);
            }
        }

        missingPaintGroups.sort(Collections.reverseOrder());
        for (Integer missingPaintGroup : missingPaintGroups) {
            paintGroups.get(missingPaintGroup).clear(editor.getMarkupModel());
            paintGroups.remove((int)missingPaintGroup);
        }

        for (PaintGroupDataBean newPaintGroup : newPaintGroupBeans) {
            paintGroups.add(new PaintGroup(newPaintGroup));
        }
    }

    /**
     *
     * @param missingPaintGroups
     * @param paintGroupProperties
     * @return true if paintGroupDataBean existed before. false if the paintGroupDataBean is a new one.
     */
    private boolean repaintIfExisted(Collection<Integer> missingPaintGroups, PaintGroupDataBean paintGroupProperties) {
        for (Integer missingPaintGroup : missingPaintGroups) {
            if (paintGroups.get(missingPaintGroup).hasSameDataBean(paintGroupProperties)) {
                missingPaintGroups.remove(missingPaintGroup);
                paintGroups.get(missingPaintGroup).repaint(paintGroupProperties, editor.getMarkupModel());
                return true;
            }
        }
        return false;
    }

    public void undoLastPaint() {
        if (undoList.isEmpty()) {
            HintManager.getInstance().showInformationHint(editor, "There is nothing to undo.\nMaybe because the document has changed.");
            setUndoEnabled(false);
            return;
        }
        for (RangeHighlighter rangeHighlighter : undoList) {
            paintGroups.get(lastPaintedGroup).highlighters.remove(rangeHighlighter);
            editor.getMarkupModel().removeHighlighter(rangeHighlighter);
        }
        clearUndoFields();
    }

    public void convertPaintGroupToSelection(int i) {
        final ArrayList<RangeHighlighter> highlighters = paintGroups.get(i).highlighters;
        if (highlighters.isEmpty()) {
            HintManager.getInstance().showInformationHint(editor, "This Paint Group is currently empty");
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


    public void navigateToNextPaint() {
        final CaretModel caretModel = editor.getCaretModel();

        final int currentCaret = caretModel.getOffset();

        // if there was a previous navigation action, try to stick to it, and navigate inside that group
        if (navigationGroup >= 0) {
            for (int i = 0; i < paintGroups.get(navigationGroup).highlighters.size(); i++) {
                if (paintGroups.get(navigationGroup).highlighters.get(i).getStartOffset() <= currentCaret
                        && paintGroups.get(navigationGroup).highlighters.get(i).getEndOffset() >= currentCaret) {
                    int next = i == paintGroups.get(navigationGroup).highlighters.size() - 1 ? 0 : i + 1;
                    caretModel.getPrimaryCaret().moveToOffset(paintGroups.get(navigationGroup).highlighters.get(next).getStartOffset());
                    editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
                    return;
                }
            }
        }

        // find the first paint group that has the caret inside one of its paints
        final int length = paintGroups.size();
        for (int i1 = 0; i1 < length; i1++) {
            PaintGroup paintGroup = paintGroups.get(i1);
            for (int i = 0; i < paintGroup.highlighters.size(); i++) {
                if (paintGroup.highlighters.get(i).getStartOffset() <= currentCaret
                        && paintGroup.highlighters.get(i).getEndOffset() >= currentCaret) {
                    int next = i == paintGroup.highlighters.size() - 1 ? 0 : i + 1;
                    caretModel.getPrimaryCaret().moveToOffset(paintGroup.highlighters.get(next).getStartOffset());
                    editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
                    navigationGroup = i1;
                    return;
                }
            }
        }

        // find the closest paint group searching downward
        int currentClosestDistance = editor.getDocument().getTextLength();
        int currentBestCaretOffset = currentCaret;
        for (int i1 = 0; i1 < length; i1++) {
            PaintGroup paintGroup = paintGroups.get(i1);
            for (int i = 0; i < paintGroup.highlighters.size(); i++) {
                if (paintGroup.highlighters.get(i).getStartOffset() >= currentCaret) {
                    if (paintGroup.highlighters.get(i).getStartOffset() - currentCaret < currentClosestDistance) {
                        currentClosestDistance = paintGroup.highlighters.get(i).getStartOffset() - currentCaret;
                        currentBestCaretOffset = paintGroup.highlighters.get(i).getStartOffset();
                        navigationGroup = i1;
                    }
                    continue;
                }
            }
        }
        caretModel.getPrimaryCaret().moveToOffset(currentBestCaretOffset);
        editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);

    }

    public void navigateToPreviousPaint() {
        final CaretModel caretModel = editor.getCaretModel();

        final int currentCaret = caretModel.getOffset();

        // if there was a previous navigation action, try to stick to it, and navigate inside that group
        if (navigationGroup >= 0) {
            for (int i = paintGroups.get(navigationGroup).highlighters.size() -1; i >= 0; i--) {
                if (paintGroups.get(navigationGroup).highlighters.get(i).getStartOffset() <= currentCaret
                        && paintGroups.get(navigationGroup).highlighters.get(i).getEndOffset() >= currentCaret) {
                    int prev = i == 0 ? paintGroups.get(navigationGroup).highlighters.size() - 1 : i - 1;
                    caretModel.getPrimaryCaret().moveToOffset(paintGroups.get(navigationGroup).highlighters.get(prev).getStartOffset());
                    editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
                    return;
                }
            }
        }

        // find the first paint group that has the caret inside one of its paints
        final int length = paintGroups.size();
        for (int i1 = 0; i1 < length; i1++) {
            PaintGroup paintGroup = paintGroups.get(i1);
            for (int i = paintGroup.highlighters.size() - 1; i >= 0; i--) {
                if (paintGroup.highlighters.get(i).getStartOffset() <= currentCaret
                        && paintGroup.highlighters.get(i).getEndOffset() >= currentCaret) {
                    int next = i == 0 ? paintGroup.highlighters.size() - 1 : i - 1;
                    caretModel.getPrimaryCaret().moveToOffset(paintGroup.highlighters.get(next).getStartOffset());
                    editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
                    navigationGroup = i1;
                    return;
                }
            }
        }

        // find the closest paint group searching downward
        int currentClosestDistance = editor.getDocument().getTextLength();
        int currentBestCaretOffset = currentCaret;
        for (int i1 = 0; i1 < length; i1++) {
            PaintGroup paintGroup = paintGroups.get(i1);
            for (int i = paintGroup.highlighters.size() -1; i >= 0; i--) {
                if (paintGroup.highlighters.get(i).getEndOffset() <= currentCaret) {
                    if (currentCaret - paintGroup.highlighters.get(i).getEndOffset() < currentClosestDistance) {
                        currentClosestDistance = currentCaret - paintGroup.highlighters.get(i).getEndOffset();
                        currentBestCaretOffset = paintGroup.highlighters.get(i).getStartOffset();
                        navigationGroup = i1;
                    }
                    continue;
                }
            }
        }
        caretModel.getPrimaryCaret().moveToOffset(currentBestCaretOffset);
        editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);

    }

    private class ClearUndoFieldsWhenChanged implements DocumentListener {
        @Override
        public void beforeDocumentChange(DocumentEvent documentEvent) {

        }

        @Override
        public void documentChanged(DocumentEvent documentEvent) {
            clearUndoFields();
        }
    }

    private void clearUndoFields() {
        undoList.clear();
        lastPaintedGroup = -1;
        setUndoEnabled(false);

    }
}

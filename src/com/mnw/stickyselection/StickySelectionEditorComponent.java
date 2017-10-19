package com.mnw.stickyselection;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.markup.*;
import com.jgoodies.common.base.Strings;
import com.mnw.stickyselection.model.PaintGroupDataBean;
import com.mnw.stickyselection.model.ValuesRepository;

import java.util.ArrayList;
import java.util.List;

public class StickySelectionEditorComponent {

    private static final int MAX_NUMBER_OF_PAINT_GROUPS = 8;
    private Editor editor;
    protected PaintGroup[] paintGroups = new PaintGroup[MAX_NUMBER_OF_PAINT_GROUPS];


    public StickySelectionEditorComponent(Editor editor) {
        this.editor = editor;
        for (int i = 0; i < MAX_NUMBER_OF_PAINT_GROUPS; i++) {
            paintGroups[i] = new PaintGroup();
        }

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


    List<RangeHighlighter> undoList = new ArrayList<>();
    int undoPaintGroup;
    public void paintSelection(int paintGroup) {
        PaintGroupDataBean paintGroupProperties = ServiceManager.getService(ValuesRepository.class).getPaintGroupProperties(paintGroup);
        if (Strings.isEmpty(editor.getSelectionModel().getSelectedText())) {
            editor.getSelectionModel().selectWordAtCaret(true);
        }
        final String selectedText = editor.getSelectionModel().getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) {
            return;
        }


        undoList.clear();
        undoPaintGroup = paintGroup;
        // // TODO: 2017. 10. 17. add this only once
        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void beforeDocumentChange(DocumentEvent documentEvent) {

            }

            @Override
            public void documentChanged(DocumentEvent documentEvent) {
                undoList.clear();
                undoPaintGroup = -1;
            }
        });

        final int lengthOfSelection = selectedText.length();
        final String wholeText = editor.getDocument().getText();

        ArrayList<Integer> selectedMatchStart = getSelectionMatchesStart(wholeText, selectedText);

        for (Integer index : selectedMatchStart) {
            final TextAttributes textAttributes = new TextAttributes();
            textAttributes.setBackgroundColor(paintGroupProperties.getColor());
            textAttributes.setEffectType(EffectType.BOXED);
            textAttributes.setEffectColor(paintGroupProperties.getColor());
            RangeHighlighter rangeHighlighter = editor.getMarkupModel().addRangeHighlighter(
                    index,
                    index + lengthOfSelection,
                    paintGroupProperties.getHughlightLayer(),
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
            paintGroups[paintGroup].add(rangeHighlighter);
        }

        paintGroups[paintGroup].highlighters.sort((o1, o2)-> ((Integer)o1.getStartOffset()).compareTo(o2.getStartOffset()));

    }

    private ArrayList<Integer> getSelectionMatchesStart(String wholeText, String selectedText) {
        ArrayList<Integer> selectedMatchStart = new ArrayList<Integer>();

        int index;
        int offset = 0;
        System.out.println(selectedText);
        while ((index = wholeText.indexOf(selectedText, offset)) > -1) {
//            System.out.println("select: index=" + index + " offset=" + offset);
            selectedMatchStart.add(index);
            offset = index + 1;
        }

        return selectedMatchStart;
    }

    public void clearPaintGroup(int paintGroup) {
        paintGroups[paintGroup].clear(editor.getMarkupModel());
    }

    public void repaint(ValuesRepository savedValues) {
        for (int i = 0; i < MAX_NUMBER_OF_PAINT_GROUPS; i++) {
            if (i >= savedValues.getPaintGroupCount()) return;
            paintGroups[i].repaint(savedValues.getPaintGroupProperties(i).getColor());
        }
    }

    public void undoLastPaint() {
        for (RangeHighlighter rangeHighlighter : undoList) {
            paintGroups[undoPaintGroup].highlighters.remove(rangeHighlighter);
            editor.getMarkupModel().removeHighlighter(rangeHighlighter);
        }
        undoList.clear();
        undoPaintGroup = -1;
    }

    public void convertPaintGroupToSelection(int i) {
        final ArrayList<RangeHighlighter> highlighters = paintGroups[i].highlighters;

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

    int navigationGroup = -1;

    public void navigateToNextPaint() {
        final CaretModel caretModel = editor.getCaretModel();

        final int currentCaret = caretModel.getOffset();

        // if there was a previous navigation action, try to stick to it, and navigate inside that group
        if (navigationGroup >= 0) {
            for (int i = 0; i < paintGroups[navigationGroup].highlighters.size(); i++) {
                if (paintGroups[navigationGroup].highlighters.get(i).getStartOffset() <= currentCaret
                        && paintGroups[navigationGroup].highlighters.get(i).getEndOffset() >= currentCaret) {
                    int next = i == paintGroups[navigationGroup].highlighters.size() - 1 ? 0 : i + 1;
                    caretModel.getPrimaryCaret().moveToOffset(paintGroups[navigationGroup].highlighters.get(next).getStartOffset());
                    return;
                }
            }
        }

        // find the first paint group that has the caret inside one of its paints
        final int length = paintGroups.length;
        for (int i1 = 0; i1 < length; i1++) {
            PaintGroup paintGroup = paintGroups[i1];
            for (int i = 0; i < paintGroup.highlighters.size(); i++) {
                if (paintGroup.highlighters.get(i).getStartOffset() <= currentCaret
                        && paintGroup.highlighters.get(i).getEndOffset() >= currentCaret) {
                    int next = i == paintGroup.highlighters.size() - 1 ? 0 : i + 1;
                    caretModel.getPrimaryCaret().moveToOffset(paintGroup.highlighters.get(next).getStartOffset());
                    navigationGroup = i1;
                    return;
                }
            }
        }

        // find the closest paint group searching downward
        for (int i1 = 0; i1 < length; i1++) {
            PaintGroup paintGroup = paintGroups[i1];
            for (int i = 0; i < paintGroup.highlighters.size(); i++) {
                if (paintGroup.highlighters.get(i).getStartOffset() >= currentCaret) {
                    caretModel.getPrimaryCaret().moveToOffset(paintGroup.highlighters.get(i).getStartOffset());
                    navigationGroup = i1;
                    return;
                }
            }
        }

    }

    public void clearAll() {


    }
}

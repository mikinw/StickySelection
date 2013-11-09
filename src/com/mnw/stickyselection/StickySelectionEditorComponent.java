package com.mnw.stickyselection;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.mnw.stickyselection.model.PaintGroupPropertiesGetter;
import com.mnw.stickyselection.model.ValuesRepositoryReader;

import java.util.ArrayList;

/**
 * TODO description of this class is missing
 */
public class StickySelectionEditorComponent {

    private static final int MAX_NUMBER_OF_PAINT_GROUPS = 8;
    private Editor editor;
    protected PaintGroup[] paintGroups = new PaintGroup[MAX_NUMBER_OF_PAINT_GROUPS];


    public StickySelectionEditorComponent(Editor editor) {
        this.editor = editor;
        for (int i = 0; i < MAX_NUMBER_OF_PAINT_GROUPS; i++) {
            paintGroups[i] = new PaintGroup();
        }
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

    public void paintSelection(PaintGroupPropertiesGetter paintGroupProperties, int paintGroup) {
        final String selectedText = editor.getSelectionModel().getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) {
            return;
        }

        final int lengthOfSelection = selectedText.length();
        final String wholeText = editor.getDocument().getText();

        ArrayList<Integer> selectedMatchStart = getSelectionMatchesStart(wholeText, selectedText);

        for (Integer index : selectedMatchStart) {
            final TextAttributes textAttributes = new TextAttributes();
            textAttributes.setBackgroundColor(paintGroupProperties.getColor());
            RangeHighlighter rangeHighlighter = editor.getMarkupModel().addRangeHighlighter(
                    index,
                    index + lengthOfSelection,
                    paintGroupProperties.getHighlightLayer(),
                    textAttributes,
                    HighlighterTargetArea.EXACT_RANGE);
            if (paintGroupProperties.isMarkerEnabled()) {
                rangeHighlighter.setErrorStripeMarkColor(paintGroupProperties.getColor());
            }

            paintGroups[paintGroup].add(rangeHighlighter);
        }

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

    public void repaint(ValuesRepositoryReader savedValues) {
        for (int i = 0; i < MAX_NUMBER_OF_PAINT_GROUPS; i++) {
            paintGroups[i].repaint(savedValues.getColorOfSelectionGroup(i));
        }
    }
}

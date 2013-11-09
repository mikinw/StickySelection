package com.mnw.stickyhighlight;

import com.google.common.base.Preconditions;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.*;
import com.mnw.stickyhighlight.model.PaintGroupPropertiesGetter;
import com.mnw.stickyhighlight.model.ValuesRepositoryReader;

import java.util.ArrayList;

/**
 * TODO description of this class is missing
 */
public class StickyHighlightEditorComponent /*implements /*CaretListener, DocumentListener*/ {

    private static final int MAX_NUMBER_OF_PAINT_GROUPS = 8;
//    private final StickyHighlightAppComponent _appComponent;
    private Editor editor;
    protected PaintGroup[] paintGroups = new PaintGroup[MAX_NUMBER_OF_PAINT_GROUPS];


    public StickyHighlightEditorComponent(/*StickyHighlightAppComponent stickyHighlightAppComponent, */Editor editor) {
//        _appComponent = stickyHighlightAppComponent;
        this.editor = editor;
//        _editor.getCaretModel().addCaretListener(this);
//        _editor.getDocument().addDocumentListener(this);
        for (int i = 0; i < MAX_NUMBER_OF_PAINT_GROUPS; i++) {
            paintGroups[i] = new PaintGroup();
        }
    }

    public void dispose() {
        clearState();
//        _editor.getCaretModel().removeCaretListener(this);
//        _editor.getDocument().removeDocumentListener(this);
        editor = null;
    }

//    @Override
//    public void caretPositionChanged(final CaretEvent caretEvent) {
//        //Execute later so we are not searching Psi model while updating it
//        //Fixes Idea 7 exception
//        ApplicationManager.getApplication().invokeLater(new Runnable() {
//            public void run() {
//                handleCaretPositionChanged(caretEvent);
//            }
//        });
//    }


    // TODO [mnw] delete this
/*    protected void handleSelectionChanged(SelectionEvent selectionEvent) {
        Project project = _editor.getProject();
        final String text1 = _editor.getDocument().getText();
//        _editor.getSelectionModel().getSelectionEndPositio
//        text1.
        final TextRange[] newRanges = selectionEvent.getNewRanges();
        StringBuilder stringBuilder = new StringBuilder();
        for (TextRange newRange : newRanges) {
            stringBuilder.append("range: ");
            stringBuilder.append(newRange.getStartOffset());
            stringBuilder.append(", ");
            stringBuilder.append(newRange.getEndOffset());
            stringBuilder.append(": ");
            stringBuilder.append(newRange.toString());
            stringBuilder.append("\n");
        }
        String text = stringBuilder.toString();
        Messages.showMessageDialog(project, text, "Information " + newRanges.length, Messages.getInformationIcon());
    }*/

/*
    public void lockIdentifiers() {
        if(_identifiersLocked)
            return;
        _identifiersLocked = true;
    }
        */

/*
    protected void handleCaretPositionChanged(CaretEvent caretEvent) {
        // TODO [mnw] uncomment these
//        if(_ignoreEvents)
//            return;
//        if(_identifiersLocked)
//            return;
        if(_editor == null)
            return;
        if(_editor.getProject() == null)
            return;
        if(_editor.getDocument() == null)
            return;
        PsiFile pFile = PsiDocumentManager.getInstance(_editor.getProject()).getPsiFile(_editor.getDocument());
        if(pFile == null)
            return;
        PsiElement pElem = pFile.findElementAt(_editor.getCaretModel().getOffset());
        if(!(pElem instanceof PsiIdentifier))
            pElem = null;
        if(pElem == null) {
//            if(_highlights != null)
//                clearState();
            return;
        }
        //We have a pElem
        //Check if different identifier than before
        if(paintGroups != null) {
*/
/*            int foundElem = -1;
            TextRange pElemRange = pElem.getTextRange();
            for(int i = 0; i < _highlights.size(); i++) {
                RangeHighlighter highlight = _highlights.get(i);
                if((highlight.getStartOffset() == pElemRange.getStartOffset()) && (highlight.getEndOffset() == pElemRange.getEndOffset())) {
                    foundElem = i;
                    break;
                }
            }
            if(foundElem != -1) {
                if(foundElem != _currElem) {
                    moveIdentifier(foundElem);
                    _startElem = foundElem;
                }
                return;
            } else
                clearState();*//*

        }
//        _currentIdentifier = pElem.getText();
        ArrayList<PsiElement> elems = new ArrayList<PsiElement>();
        PsiReference pRef = pFile.findReferenceAt(_editor.getCaretModel().getOffset());
        if(pRef == null) {
*/
/*            //See if I am a declaration so search for references to me
            PsiElement pElemCtx = pElem.getContext();
            if(pElemCtx instanceof PsiClass)
                _elemType = ELEMENT_TYPE.CLASS;
            else if(pElemCtx instanceof PsiMethod)
                _elemType = ELEMENT_TYPE.METHOD;
            else if(pElemCtx instanceof PsiField)
                _elemType = ELEMENT_TYPE.FIELD;
            else if(pElemCtx instanceof PsiParameter)
                _elemType = ELEMENT_TYPE.PARAMETER;
            else if(pElemCtx instanceof PsiLocalVariable)
                _elemType = ELEMENT_TYPE.LOCAL;
            Query<PsiReference> q = ReferencesSearch.search(pElemCtx, GlobalSearchScope.fileScope(pFile));
            PsiReference qRefs[] = q.toArray(new PsiReference[0]);
            //Sort by text offset
            Arrays.sort(qRefs, _psiRefComp);
            for(PsiReference qRef : qRefs) {
                //Find child PsiIdentifier so highlight is just on it
                PsiElement qRefElem = qRef.getElement();
                PsiIdentifier qRefElemIdent = findChildIdentifier(qRefElem,pElem.getText());
                if(qRefElemIdent == null)
                    continue;
                //Skip elements from other files
                if(!areSameFiles(pFile,qRefElemIdent.getContainingFile()))
                    continue;
                //Check if I should be put in list first to keep it sorted by text offset
                if((_declareElem == -1) && (pElem.getTextOffset() <= qRefElemIdent.getTextOffset())) {
                    elems.add(pElem);
                    _declareElem = elems.size() - 1;
                }
                elems.add(qRefElemIdent);
            }
            //If haven't put me in list yet, put me in last
            if(_declareElem == -1) {
                elems.add(pElem);
                _declareElem = elems.size() - 1;
            }*//*

                elems.add(pElem);
        } else {
*/
/*            //Resolve to declaration
            PsiElement pRefElem;
            try {
                pRefElem = pRef.resolve();
            } catch(Throwable t) {
                pRefElem = null;
            }
            if(pRefElem != null) {
                if(pRefElem instanceof PsiClass)
                    _elemType = ELEMENT_TYPE.CLASS;
                else if(pRefElem instanceof PsiMethod)
                    _elemType = ELEMENT_TYPE.METHOD;
                else if(pRefElem instanceof PsiField)
                    _elemType = ELEMENT_TYPE.FIELD;
                else if(pRefElem instanceof PsiParameter)
                    _elemType = ELEMENT_TYPE.PARAMETER;
                else if(pRefElem instanceof PsiLocalVariable)
                    _elemType = ELEMENT_TYPE.LOCAL;
            }
            if(pRefElem != null) {
                PsiIdentifier pRefElemIdent = findChildIdentifier(pRefElem,pElem.getText());
                if(pRefElemIdent != null) {
                    //Search for references to my declaration
                    Query<PsiReference> q = ReferencesSearch.search(pRefElemIdent.getContext(),GlobalSearchScope.fileScope(pFile));
                    PsiReference qRefs[] = q.toArray(new PsiReference[0]);
                    //Sort by text offset
                    Arrays.sort(qRefs,_psiRefComp);
                    for(PsiReference qRef : qRefs) {
                        //Find child PsiIdentifier so highlight is just on it
                        PsiElement qRefElem = qRef.getElement();
                        PsiIdentifier qRefElemIdent = findChildIdentifier(qRefElem,pElem.getText());
                        if(qRefElemIdent == null)
                            continue;
                        //Skip elements from other files
                        if(!areSameFiles(pFile,qRefElemIdent.getContainingFile()))
                            continue;
                        //Check if I should be put in list first to keep it sorted by text offset
                        if((areSameFiles(pFile,pRefElemIdent.getContainingFile())) && (_declareElem == -1) && (pRefElemIdent.getTextOffset() <= qRefElemIdent.getTextOffset())) {
                            elems.add(pRefElemIdent);
                            _declareElem = elems.size() - 1;
                        }
                        elems.add(qRefElemIdent);
                    }
                    if(elems.size() == 0) {
                        //Should at least put the original found element at cursor in list
                        //Check if I should be put in list first to keep it sorted by text offset
                        if((areSameFiles(pFile,pRefElemIdent.getContainingFile())) && (_declareElem == -1) && (pRefElemIdent.getTextOffset() <= pElem.getTextOffset())) {
                            elems.add(pRefElemIdent);
                            _declareElem = elems.size() - 1;
                        }
                        elems.add(pElem);
                    }
                    //If haven't put me in list yet, put me in last
                    if((areSameFiles(pFile,pRefElemIdent.getContainingFile())) && (_declareElem == -1)) {
                        elems.add(pRefElemIdent);
                        _declareElem = elems.size() - 1;
                    }
                }
            } else {
                //No declaration found, so resort to simple string search
                PsiSearchHelper search = pElem.getManager().getSearchHelper();
                PsiElement idents[] = search.findIdentifiers(pElem.getText(),GlobalSearchScope.fileScope(pFile), UsageSearchContext.ANY);
                for(PsiElement ident : idents)
                    elems.add(ident);
            }*//*

        }
//        _forWriting = new ArrayList<Boolean>();
        for(int i = 0; i < elems.size(); i++) {
            PsiElement elem = elems.get(i);
            TextRange range = elem.getTextRange();
            //Verify range is valid against current length of document
            if((range.getStartOffset() >= _editor.getDocument().getTextLength()) || (range.getEndOffset() >= _editor.getDocument().getTextLength()))
                continue;
//            boolean forWriting = isForWriting(elem);
//            _forWriting.add(forWriting);
            RangeHighlighter rh;
            final boolean ignored = true;
            rh = _editor.getMarkupModel().addRangeHighlighter(
                    range.getStartOffset(),
                    range.getEndOffset(),
                    getHighlightLayer(),
                    getActiveHighlightColor(ignored),
                    HighlighterTargetArea.EXACT_RANGE);
            rh.setErrorStripeMarkColor(getActiveHighlightColor(ignored).getBackgroundColor());
*/
/*
            if(elem.getTextRange().equals(pElem.getTextRange())) {
                _startElem = i;
                _currElem = i;
                rh = _editor.getMarkupModel().addRangeHighlighter(range.getStartOffset(),range.getEndOffset(),getHighlightLayer(),getActiveHighlightColor(forWriting),
                                                                  HighlighterTargetArea.EXACT_RANGE);
                if(_appComponent.is_showInMarkerBar())
                    rh.setErrorStripeMarkColor(getActiveHighlightColor(forWriting).getBackgroundColor());
            } else {
                rh = _editor.getMarkupModel().addRangeHighlighter(range.getStartOffset(),range.getEndOffset(),getHighlightLayer(),getHighlightColor(forWriting),HighlighterTargetArea.EXACT_RANGE);
                if(_appComponent.is_showInMarkerBar())
                    rh.setErrorStripeMarkColor(getHighlightColor(forWriting).getBackgroundColor());
            }
            if(_appComponent.is_showInMarkerBar())
                rh.setErrorStripeTooltip(_currentIdentifier + " [" + i + "]");
*//*

//            paintGroups.add(rh);
        }
    }
*/


/*    @Override
    public void beforeDocumentChange(DocumentEvent event) {
    }*/

/*    @Override
    public void documentChanged(DocumentEvent event) {
//        if(_ignoreEvents)
//            return;
        caretPositionChanged(null);
    }*/

    protected void clearState()
    {
        // TODO [mnw] uncomment these
        final MarkupModel markupModel = editor.getMarkupModel();
        for (PaintGroup paintGroup : paintGroups) {
            paintGroup.clear(markupModel);
        }
/*
        _forWriting = null;
        _currentIdentifier = null;
        _elemType = null;
        _startElem = -1;
        _currElem = -1;
        _declareElem = -1;
        unlockIdentifiers();
*/
    }

/*
    protected void moveIdentifier(int index)
    {
        // TODO [mnw] uncomment this
*/
/*        try {
            if(_currElem != -1) {
                RangeHighlighter rh = _highlights.get(_currElem);
                boolean forWriting = _forWriting.get(_currElem);
                int startOffset = rh.getStartOffset();
                int endOffset = rh.getEndOffset();
                _editor.getMarkupModel().removeHighlighter(rh);
                rh = _editor.getMarkupModel().addRangeHighlighter(startOffset,endOffset,getHighlightLayer(),getHighlightColor(forWriting),HighlighterTargetArea.EXACT_RANGE);
                if(_appComponent.is_showInMarkerBar()) {
                    rh.setErrorStripeMarkColor(getHighlightColor(forWriting).getBackgroundColor());
                    rh.setErrorStripeTooltip(_currentIdentifier + " [" + _currElem + "]");
                }
                _highlights.set(_currElem,rh);
            }
            _currElem = index;
            RangeHighlighter rh = _highlights.get(_currElem);
            boolean forWriting = _forWriting.get(_currElem);
            int startOffset = rh.getStartOffset();
            int endOffset = rh.getEndOffset();
            _editor.getMarkupModel().removeHighlighter(rh);
            rh = _editor.getMarkupModel().addRangeHighlighter(startOffset,endOffset,getHighlightLayer(),getActiveHighlightColor(forWriting),HighlighterTargetArea.EXACT_RANGE);
            if(_appComponent.is_showInMarkerBar()) {
                rh.setErrorStripeMarkColor(getActiveHighlightColor(forWriting).getBackgroundColor());
                rh.setErrorStripeTooltip(_currentIdentifier + " [" + _currElem + "]");
            }
            _highlights.set(_currElem,rh);
        } catch(Throwable t) {
            //Ignore
        }*//*

    }
*/

/*
    protected TextAttributes getActiveHighlightColor(boolean forWriting)
    {
        TextAttributes retVal = new TextAttributes();
//        if(!isHighlightEnabled())
//            return(retVal);
        int red = 120;
        int green = 40;
        int blue = 180;
        Color c = new Color(red,green,blue);
        if(_elemType == ELEMENT_TYPE.CLASS)
            c = IdentifierHighlighterConfiguration.getColorFromString(_appComponent.get_classActiveHighlightColor());
        else if(_elemType == ELEMENT_TYPE.METHOD)
            c = IdentifierHighlighterConfiguration.getColorFromString(_appComponent.get_methodActiveHighlightColor());
        else if(_elemType == ELEMENT_TYPE.FIELD)
            c = IdentifierHighlighterConfiguration.getColorFromString(forWriting ? _appComponent.get_fieldWriteActiveHighlightColor() : _appComponent.get_fieldReadActiveHighlightColor());
        else if(_elemType == ELEMENT_TYPE.PARAMETER)
            c = IdentifierHighlighterConfiguration.getColorFromString(forWriting ? _appComponent.get_paramWriteActiveHighlightColor() : _appComponent.get_paramReadActiveHighlightColor());
        else if(_elemType == ELEMENT_TYPE.LOCAL)
            c = IdentifierHighlighterConfiguration.getColorFromString(forWriting ? _appComponent.get_localWriteActiveHighlightColor() : _appComponent.get_localReadActiveHighlightColor());
        else
            c = IdentifierHighlighterConfiguration.getColorFromString(_appComponent.get_otherActiveHighlightColor());
        retVal.setBackgroundColor(c);
        return(retVal);
    }
*/

    /*
    protected int getHighlightLayer()
    {
        String highlightLayer = _appComponent.getHighlightLayer();
        if(highlightLayer.equals("SELECTION"))
            return(HighlighterLayer.SELECTION);
        else if(highlightLayer.equals("ERROR"))
            return(HighlighterLayer.ERROR);
        else if(highlightLayer.equals("WARNING"))
            return(HighlighterLayer.WARNING);
        else if(highlightLayer.equals("GUARDED_BLOCKS"))
            return(HighlighterLayer.GUARDED_BLOCKS);
        else if(highlightLayer.equals("ADDITIONAL_SYNTAX"))
            return(HighlighterLayer.ADDITIONAL_SYNTAX);
        else if(highlightLayer.equals("SYNTAX"))
            return(HighlighterLayer.SYNTAX);
        else if(highlightLayer.equals("CARET_ROW"))
            return(HighlighterLayer.CARET_ROW);
        return(HighlighterLayer.ADDITIONAL_SYNTAX);
    }
    */

    public void paintSelection(PaintGroupPropertiesGetter paintGroupProperties, int paintGroup) {
        final String selectedText = editor.getSelectionModel().getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) {
            return;
            // TODO [mnw] get pElem
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
            System.out.println("select: index=" + index + " offset=" + offset);
            selectedMatchStart.add(index);
            offset = index + 1;
        }

        return selectedMatchStart;
    }

//    private TextAttributes getPaintSelectionColor(ValuesRepositoryReader savedValues) {
//        TextAttributes retVal = new TextAttributes();
////        if(!isHighlightEnabled())
////            return(retVal);
//        int red = 30;
//        int green = 90;
//        int blue = 140;
//        JBColor c = new JBColor(new Color(red, green, blue), new Color(160, 80, 220));
//        retVal.setBackgroundColor(c);
//        return(retVal);
//
//    }

    public void clearPaintGroup(int paintGroup) {
        paintGroups[paintGroup].clear(editor.getMarkupModel());
    }

    public void repaint(ValuesRepositoryReader savedValues) {
        for (int i = 0; i < MAX_NUMBER_OF_PAINT_GROUPS; i++) {
            paintGroups[i].repaint(savedValues.getColorOfSelectionGroup(i));
        }
    }
}

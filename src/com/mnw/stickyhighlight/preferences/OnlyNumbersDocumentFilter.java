package com.mnw.stickyhighlight.preferences;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * from Carlos Heuberger
 * http://stackoverflow.com/questions/5662651/how-to-implement-in-java-jtextfield-class-to-allow-entering-only-digits
 */
public class OnlyNumbersDocumentFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int off, String str, AttributeSet attr) throws BadLocationException {
        fb.insertString(off, str.replaceAll("\\D++", ""), attr);  // remove non-digits
    }
    @Override
    public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr) throws BadLocationException {
        fb.replace(off, len, str.replaceAll("\\D++", ""), attr);  // remove non-digits
    }
}

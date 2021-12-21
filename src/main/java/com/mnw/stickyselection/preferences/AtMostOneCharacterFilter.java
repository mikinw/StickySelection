package com.mnw.stickyselection.preferences;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class AtMostOneCharacterFilter extends DocumentFilter {
    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offset, String string,
                             AttributeSet attr) throws BadLocationException {
        final String newString = string.isEmpty() ? string : string.substring(0, 1);
        fb.replace(0, fb.getDocument().getLength(), newString.toUpperCase(), attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {
        final String newString = text.isEmpty() ? text : text.substring(0, 1);
        super.replace(fb, 0, fb.getDocument().getLength(), newString.toUpperCase(), attrs);
    }
}

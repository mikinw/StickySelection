package com.mnw.stickyselection.preferences;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class OnlyOneCharacterFilter extends DocumentFilter {
    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offset, String string,
                             AttributeSet attr) throws BadLocationException {
        fb.replace(0, fb.getDocument().getLength(), string.substring(0, 1).toUpperCase(), attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {
        super.replace(fb, 0, fb.getDocument().getLength(), text.substring(0, 1).toUpperCase(), attrs);
    }
}

package com.penapereira.cipher.view.swing.component;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JTextPaneSizeFilter extends DocumentFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());
    int maxCharacters;

    public JTextPaneSizeFilter(int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    public void insertString(FilterBypass filterBypass, int offset, String str, AttributeSet attributeSet)
            throws BadLocationException {
        log.trace("Inserting string in document");

        // This rejects the entire insertion if it would make
        // the contents too long. Another option would be
        // to truncate the inserted string so the contents
        // would be exactly maxCharacters in length.
        if ((filterBypass.getDocument().getLength() + str.length()) <= maxCharacters)
            super.insertString(filterBypass, offset, str, attributeSet);
        else
            Toolkit.getDefaultToolkit().beep();
    }

    public void replace(FilterBypass filterBypass, int offset, int length, String str, AttributeSet attributeSet)
            throws BadLocationException {
        log.trace("Replacing text in document");
        // This rejects the entire replacement if it would make
        // the contents too long. Another option would be
        // to truncate the replacement string so the contents
        // would be exactly maxCharacters in length.
        if ((filterBypass.getDocument().getLength() + str.length() - length) <= maxCharacters)
            super.replace(filterBypass, offset, length, str, attributeSet);
        else
            Toolkit.getDefaultToolkit().beep();
    }

}

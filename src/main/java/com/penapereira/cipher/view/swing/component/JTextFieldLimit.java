package com.penapereira.cipher.view.swing.component;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class JTextFieldLimit extends PlainDocument {

    private static final long serialVersionUID = 1L;
    protected int limit;

    public JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }

    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null || getLength() == limit) {
            return;
        }
        int strSize = Math.min(str.length(), limit - getLength());
        super.insertString(offset, str.substring(0, strSize), attr);
    }
}

package com.penapereira.cipher.view.swing.listener;

import javax.swing.JTabbedPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CipherDocumentListener implements DocumentListener {

    private static final Logger log = LoggerFactory.getLogger(CipherDocumentListener.class);
    protected JTabbedPane tabbedPane;

    @Override
    public void insertUpdate(DocumentEvent e) {
        setModified();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        setModified();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        setModified();
    }

    protected void setModified() {
        if (tabbedPane != null) {
            log.trace("Document changed!");
            int i = tabbedPane.getSelectedIndex();
            tabbedPane.setTitleAt(i, "* " + tabbedPane.getTitleAt(i));
        }
    }

    public void setTabbedPane(JTabbedPane documentsTabbedPane) {
        this.tabbedPane = documentsTabbedPane;
    }
}

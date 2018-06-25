package com.penapereira.cipher.view.swing.listener;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.view.swing.datamodel.DatamodelInterface;

public class CipherDocumentListener implements DocumentListener {
    private static final Logger log = LoggerFactory.getLogger(CipherDocumentListener.class);

    protected DatamodelInterface<JTabbedPane, JScrollPane, JTextPane> datamodel;

    protected JTextPane textPane;

    public CipherDocumentListener(JTextPane textPane) {
        super();
        this.textPane = textPane;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        log.trace("insertUpdate");
        setModified();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        log.trace("removeUpdate");
        setModified();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        log.trace("changedUpdate");
        setModified();
    }

    protected void setModified() {
        datamodel.setModifiedNameFor(textPane);
    }

    public void setDatamodel(DatamodelInterface<JTabbedPane, JScrollPane, JTextPane> datamodel) {
        this.datamodel = datamodel;
    }
}

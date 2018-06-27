package com.penapereira.cipher.view.swing.listener;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.view.swing.datamodel.SwingDatamodelInterface;

public class CipherDocumentListener implements DocumentListener, FocusListener {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private Long documentId;
    private SwingDatamodelInterface datamodel;

    public CipherDocumentListener(SwingDatamodelInterface datamodel, Long documentId) {
        this.datamodel = datamodel;
        this.documentId = documentId;
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

    protected void setModified() {
        datamodel.setModifiedNameFor(documentId);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {}

    @Override
    public void focusGained(FocusEvent e) {}

    @Override
    public void focusLost(FocusEvent e) {}
}

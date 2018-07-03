package com.penapereira.cipher.view.swing.listener;

import java.awt.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentController;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.view.swing.datamodel.SwingDatamodelInterface;

public class RenameDocumentDelegate extends SingleInputDialogActionListener {

    private static final long serialVersionUID = 1L;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private SwingDatamodelInterface datamodel;

    public RenameDocumentDelegate(DocumentController documentController, Messages messages,
            SwingDatamodelInterface datamodel) {
        super(documentController, messages);
        this.datamodel = datamodel;
    }

    @Override
    protected void actionPerformedDelegate(ActionEvent e) {
        Long documentId = datamodel.getDocumentIdFor(datamodel.getSelectedComponent());
        Document doc = documentController.get(documentId);
        setTextFieldString(doc.getTitle());
    }

    @Override
    protected void buildDelegate() {
        setTitle(messages.getRenameDocumentMenu());
        setTextFieldLabel(messages.getNewDocumentTitle());
        setErrorText(messages.getDocumentTitleEmpty());
        setOkButtonText(messages.getRename());
    }

    @Override
    protected void okButtonPressedDelegate(String documentTitle) {
        if (!documentTitle.isEmpty()) {
            log.info("Renaming document to new title: " + documentTitle);
            Long documentId = datamodel.getDocumentIdFor(datamodel.getSelectedComponent());
            Document doc = documentController.get(documentId);
            doc.setTitle(documentTitle);
            if (doc != null) {
                documentController.save(doc);
            }
        }
    }
}

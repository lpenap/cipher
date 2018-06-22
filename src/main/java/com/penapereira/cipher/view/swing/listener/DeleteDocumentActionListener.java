package com.penapereira.cipher.view.swing.listener;

import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentController;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.shared.SwingUtil;
import com.penapereira.cipher.view.swing.datamodel.DatamodelInterface;

public class DeleteDocumentActionListener extends AbstractActionListener {

    private static final Logger log = LoggerFactory.getLogger(DeleteDocumentActionListener.class);
    private static final long serialVersionUID = 1L;
    private DatamodelInterface<JTabbedPane, JScrollPane, JTextPane> datamodel;
    private JFrame parent;

    public DeleteDocumentActionListener(DocumentController documentController, Messages messages, JFrame parent,
            DatamodelInterface<JTabbedPane, JScrollPane, JTextPane> datamodel) {
        super(documentController, messages);
        this.datamodel = datamodel;
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Document selectedDoc = getSelectedDocument();
        if (selectedDoc == null) {
            log.warn("Selected document is null, refreshing all documents.");
            documentController.requestNotifyObservers();
        } else {
            confirmDeletion(selectedDoc);
        }
    }

    private void confirmDeletion(Document doc) {
        String confirmMessage =
                messages.getDeleteDocumentConfirmPre() + doc.getTitle() + messages.getDeleteDocumentConfirmPost();
        boolean isDeletionConfirmed = new SwingUtil(parent).confirm(messages.getDeleteDocumentMenu(), confirmMessage);
        if (isDeletionConfirmed) {
            log.debug("Deleting document " + doc.getTitle());
            documentController.delete(doc);
        }
    }

    protected Long getSelectedDocumentId() {
        return datamodel.getDocumentIdFor((JScrollPane) datamodel.getWrappedDatamodel().getSelectedComponent());
    }

    protected Document getSelectedDocument() {
        Document selectedDocument = null;
        Long documentId = getSelectedDocumentId();
        if (documentId != null) {
            selectedDocument = documentController.get(documentId);
        }
        return selectedDocument;
    }

    @Override
    protected void build() {}
}

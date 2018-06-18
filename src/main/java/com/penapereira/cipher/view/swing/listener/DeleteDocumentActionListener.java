package com.penapereira.cipher.view.swing.listener;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentController;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.view.swing.MainUserInterfaceImpl;

public class DeleteDocumentActionListener extends AbstractActionListener {

    private static final Logger log = LoggerFactory.getLogger(DeleteDocumentActionListener.class);
    private static final long serialVersionUID = 1L;
    private MainUserInterfaceImpl parent;

    public DeleteDocumentActionListener(DocumentController documentController, Messages messages,
            MainUserInterfaceImpl parent) {
        super(documentController, messages);
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
        int dialogResult = JOptionPane.showConfirmDialog(null, messages.getDeleteDocumentMenu(),
                messages.getDeleteDocumentConfirmPre() + doc.getTitle() + messages.getDeleteDocumentConfirmPost(),
                JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            documentController.delete(doc);
        }
    }

    protected Long getSelectedDocumentId() {
        JScrollPane selectedComponent = (JScrollPane) parent.getDocumentsTabbedPane().getSelectedComponent();
        return parent.getDocumentIdFromScrollPane(selectedComponent);
    }

    protected Document getSelectedDocument() {
        Document selectedDocument = null;
        Long documentId = getSelectedDocumentId();
        if (documentId != null) {
            selectedDocument = documentController.get(documentId);
        }
        return selectedDocument;
    }

    protected void deleteSelectedDocument() {
        Long documentId = getSelectedDocumentId();
        if (documentId != null) {
            documentController.delete(documentId);
        }
    }

    @Override
    protected void build() {}
}

package com.penapereira.cipher.view.swing.listener;

import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentController;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.util.SwingUtil;
import com.penapereira.cipher.view.swing.datamodel.SwingDataModelInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class DeleteDocumentActionListener extends AbstractActionListener {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final long serialVersionUID = 1L;
    private final SwingDataModelInterface dataModel;
    private final JFrame parent;

    public DeleteDocumentActionListener(DocumentController documentController, Messages messages, JFrame parent,
                                        SwingDataModelInterface dataModel) {
        super(documentController, messages);
        this.dataModel = dataModel;
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getSelectedDocument().ifPresentOrElse(this::confirmDeletion,
                () -> {
                    log.warn("Selected document is null, refreshing all documents.");
                    documentController.fireUpdateAll();
                });
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
        JComponent selectedComponent = dataModel.getSelectedComponent();
        return dataModel.getDocumentIdFor(selectedComponent);
    }

    protected Optional<Document> getSelectedDocument() {
        Document selectedDocument = null;
        Long documentId = getSelectedDocumentId();
        return documentController.get(documentId);
    }

    @Override
    protected void build() {
    }
}

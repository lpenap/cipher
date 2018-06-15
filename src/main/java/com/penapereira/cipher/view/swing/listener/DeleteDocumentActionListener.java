package com.penapereira.cipher.view.swing.listener;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentController;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.view.swing.MainUserInterfaceImpl;

public class DeleteDocumentActionListener extends AbstractActionListener {

    private static final long serialVersionUID = 1L;
    private MainUserInterfaceImpl parent;

    public DeleteDocumentActionListener(DocumentController documentController, Messages messages,
            MainUserInterfaceImpl parent) {
        super(documentController, messages);
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int dialogResult = JOptionPane.showConfirmDialog(null, messages.getDeleteDocumentMenu(),
                messages.getDeleteDocumentConfirmPre() + e.get, JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            deleteSelectedDocument();
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

package com.penapereira.cipher.view.swing.listener;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentController;

public class DeleteDocumentActionListener extends AbstractActionListener {

    private static final long serialVersionUID = 1L;

    public DeleteDocumentActionListener(DocumentController documentController, Messages messages, ) {
        super(documentController, messages);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int dialogResult = JOptionPane.showConfirmDialog(null, messages.getDeleteDocumentMenu(), messages.getDeleteDocumentConfirmPre() + e.get, JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            removeDocument();
        }
    }

    protected void removeDocument() {

    }

    @Override
    protected void build() {}
}

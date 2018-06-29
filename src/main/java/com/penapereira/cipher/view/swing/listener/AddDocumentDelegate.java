package com.penapereira.cipher.view.swing.listener;

import java.awt.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentController;

public class AddDocumentDelegate extends SingleInputDialogActionListener {

    private static final long serialVersionUID = 1L;
    private final Logger log = LoggerFactory.getLogger(AddDocumentDelegate.class);

    public AddDocumentDelegate(DocumentController documentController, Messages messages) {
        super(documentController, messages);
    }

    protected void buildDelegate() {
        setTitle(messages.getAddDocumentMenu());
        setTextFieldLabel(messages.getDocumentTitle());
        setErrorText(messages.getDocumentTitleEmpty());
        setOkButtonText(messages.getCreate());
    }

    @Override
    protected void actionPerformedDelegate(ActionEvent e) {
        getTextField().setRequestFocusEnabled(true);
    }

    @Override
    protected void okButtonPressedDelegate(String documentTitle) {
        log.info("Adding new empty document with title: " + documentTitle);
        documentController.createAndSaveDocument(documentTitle, "");
    }

}

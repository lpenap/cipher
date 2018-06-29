package com.penapereira.cipher.view.swing.listener;

import java.awt.event.ActionEvent;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentController;

public abstract class SingleInputDialogActionListener extends AbstractActionListener {

    private static final long serialVersionUID = 1L;

    public SingleInputDialogActionListener(DocumentController documentController, Messages messages) {
        super(documentController, messages);
    }

    protected abstract void actionPerformedDelegate(ActionEvent e);

    protected abstract void buildDelegate();

    @Override
    public void actionPerformed(ActionEvent e) {
        actionPerformedDelegate(e);
    }

    @Override
    protected void build() {
        buildDelegate();
    }

}

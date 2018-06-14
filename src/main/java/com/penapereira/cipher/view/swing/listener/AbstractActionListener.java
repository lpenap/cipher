package com.penapereira.cipher.view.swing.listener;

import java.awt.event.ActionListener;
import javax.swing.JFrame;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentController;

public abstract class AbstractActionListener extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    protected DocumentController documentController;
    protected Messages messages;

    protected AbstractActionListener() {
        build();
    }

    protected AbstractActionListener(DocumentController documentController, Messages msgs) {
        this.documentController = documentController;
        this.messages = msgs;
        build();
    }

    /**
     * Factory method invoked from abstract constructor.
     */
    abstract protected void build();
}

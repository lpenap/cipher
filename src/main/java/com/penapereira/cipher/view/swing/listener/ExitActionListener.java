package com.penapereira.cipher.view.swing.listener;

import java.awt.event.ActionEvent;

public class ExitActionListener extends AbstractActionListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }

    @Override
    protected void build() {}

}

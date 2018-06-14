package com.penapereira.cipher.view.swing.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.shared.Util;

public class AboutActionListener extends AbstractActionListener {

    private static final long serialVersionUID = 1L;

    public AboutActionListener(Messages messages) {
        super(null, messages);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog((Component) e.getSource(), new Util().listToString(messages.getAbout()),
                messages.getAboutMenu(), JOptionPane.OK_OPTION);
    }

    @Override
    protected void build() {}

}

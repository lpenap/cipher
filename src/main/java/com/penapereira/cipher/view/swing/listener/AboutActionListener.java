package com.penapereira.cipher.view.swing.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.springframework.beans.factory.annotation.Autowired;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.shared.Util;

public class AboutActionListener implements ActionListener {

    protected Messages messages;

    @Autowired
    public AboutActionListener(Messages messages) {
        super();
        this.messages = messages;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog((Component) e.getSource(), new Util().listToString(messages.getAbout()),
                messages.getAboutMenu(), JOptionPane.OK_OPTION);
    }

}

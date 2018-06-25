package com.penapereira.cipher.shared;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class SwingUtil {

    protected Component parent;

    public SwingUtil(Component parent) {
        this.parent = parent;
    }

    public SwingUtil() {}

    public void alert(String msg) {
        JOptionPane.showMessageDialog(parent, msg);
    }

    public boolean confirm(String title, String msg) {
        int dialogResult = JOptionPane.showConfirmDialog(parent, msg, title, JOptionPane.YES_NO_OPTION);
        return dialogResult == JOptionPane.YES_OPTION;
    }

    public JButton createTransparentButton(Icon icon, String toolTip) {
        JButton button = new JButton(icon);
        button.setToolTipText(toolTip);
        button.setBorder(null);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        return button;
    }
}

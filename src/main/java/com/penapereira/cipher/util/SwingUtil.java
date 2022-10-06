package com.penapereira.cipher.util;

import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

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

    public AttributeSet getDefaultAttributeSet() {
        return getAttributeSet(Color.WHITE, Color.BLUE);
    }

    public AttributeSet getAltAttributeSet() {
        return getAttributeSet(Color.YELLOW, Color.BLACK);
    }

    public AttributeSet getAttributeSet(Color background, Color foreground) {
        StyleContext defaultStyle = StyleContext.getDefaultStyleContext();
        AttributeSet set = defaultStyle.addAttribute(defaultStyle.getEmptySet(), StyleConstants.Background, background);
        set = defaultStyle.addAttribute(set, StyleConstants.Foreground, foreground);
        return set;
    }
}

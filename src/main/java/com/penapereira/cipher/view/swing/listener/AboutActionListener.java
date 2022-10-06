package com.penapereira.cipher.view.swing.listener;

import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.util.StringUtil;
import com.penapereira.cipher.util.SwingUtil;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URISyntaxException;

public class AboutActionListener extends AbstractActionListener {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final long serialVersionUID = 1L;
    private JEditorPane aboutPane;

    public AboutActionListener(Messages messages) {
        super(null, messages);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IconFontSwing.register(FontAwesome.getIconFont());
        Icon icon = IconFontSwing.buildIcon(FontAwesome.LOCK, 32, Color.RED);
        JOptionPane.showMessageDialog((Component) e.getSource(), aboutPane, messages.getAboutMenu(),
                JOptionPane.INFORMATION_MESSAGE, icon);
    }

    @Override
    protected void build() {
        JLabel label = new JLabel();
        Font font = label.getFont();
        String style = "font-family:" + font.getFamily() + ";" +
                "font-weight:" + (font.isBold() ? "bold" : "normal") + ";" +
                "font-size:" + font.getSize() + "pt;";

        aboutPane = new JEditorPane("text/html", "<html><body style=\"" + style + "\">" //
                + new StringUtil().linesToPage(messages.getAbout()) //
                + "</body></html>");

        aboutPane.addHyperlinkListener(e -> {
            if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (IOException e1) {
                    new SwingUtil().alert("Default browser could not be found!");
                } catch (URISyntaxException e1) {
                    log.error("The URL for the project could not be parsed, this is a bug");
                }
        });
        aboutPane.setEditable(false);
        aboutPane.setBackground(label.getBackground());
    }
}

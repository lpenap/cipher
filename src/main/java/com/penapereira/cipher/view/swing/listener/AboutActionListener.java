package com.penapereira.cipher.view.swing.listener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.Icon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.shared.StringUtil;
import com.penapereira.cipher.shared.SwingUtil;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;

public class AboutActionListener extends AbstractActionListener {

    private Logger log = LoggerFactory.getLogger(getClass());
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
                JOptionPane.OK_OPTION, icon);
    }

    @Override
    protected void build() {
        JLabel label = new JLabel();
        Font font = label.getFont();
        StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
        style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
        style.append("font-size:" + font.getSize() + "pt;");

        aboutPane = new JEditorPane("text/html", "<html><body style=\"" + style + "\">" //
                + new StringUtil().listToString(messages.getAbout()) //
                + "</body></html>");

        aboutPane.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (IOException e1) {
                        new SwingUtil().alert("Default browser could not be found!");
                    } catch (URISyntaxException e1) {
                        log.error("The URL for the project could not be parsed, this is a bug");
                    }
            }
        });
        aboutPane.setEditable(false);
        aboutPane.setBackground(label.getBackground());
    }
}

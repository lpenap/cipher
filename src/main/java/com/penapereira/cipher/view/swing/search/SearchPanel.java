package com.penapereira.cipher.view.swing.search;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SearchPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField searchTextField;

    public SearchPanel() {
        super();
        setLayout(new BorderLayout(0, 0));

        JPanel eastPanel = new JPanel();
        add(eastPanel, BorderLayout.EAST);

        searchTextField = new JTextField();
        eastPanel.add(searchTextField);
        searchTextField.setColumns(10);

        IconFontSwing.register(FontAwesome.getIconFont());
        Icon iconPrevious = IconFontSwing.buildIcon(FontAwesome.ANGLE_UP, 20, new Color(0, 0, 0));
        Icon iconNext = IconFontSwing.buildIcon(FontAwesome.ANGLE_DOWN, 20, new Color(0, 0, 0));
        Icon iconClose = IconFontSwing.buildIcon(FontAwesome.WINDOW_CLOSE_O, 20, new Color(0, 0, 0));

        JLabel labelPrevious = new JLabel(iconPrevious);
        eastPanel.add(labelPrevious);

        JLabel labelNext = new JLabel(iconNext);
        eastPanel.add(labelNext);

        JLabel labelClose = new JLabel(iconClose);
        eastPanel.add(labelClose);
        
        setVisible(false);
    }

}

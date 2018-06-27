package com.penapereira.cipher.view.swing.search;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.shared.SwingUtil;
import com.penapereira.cipher.view.swing.component.JTextFieldLimit;
import com.penapereira.cipher.view.swing.datamodel.SwingDatamodelInterface;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;

public class SearchPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final int MAX_SEARCH_QUERY_LENGTH = 100;
    private final int SEARCH_INPUT_TEXT_COLUMNS = 20;

    private JTextField searchTextField;
    private Messages messages;
    private JLabel labelSearchFound;
    private JLabel labelSearchTotal;
    private SwingDatamodelInterface datamodel;
    private SearchMonitor searchMonitor;

    public SearchPanel(Messages messages, SwingDatamodelInterface datamodel) {
        super();
        this.messages = messages;
        this.datamodel = datamodel;
        this.searchMonitor = new SearchMonitor();

        setLayout(new BorderLayout(0, 0));
        JPanel eastPanel = new JPanel();
        add(eastPanel, BorderLayout.EAST);

        addSearchTextField(eastPanel);
        addSearchResultLabels(eastPanel);
        addSearchControlButtons(eastPanel);
        addSearchAdapter();
        setVisible(false);
    }

    protected void addSearchTextField(JPanel eastPanel) {
        searchTextField = new JTextField();
        searchTextField.setColumns(SEARCH_INPUT_TEXT_COLUMNS);
        searchTextField.setDocument(new JTextFieldLimit(MAX_SEARCH_QUERY_LENGTH));
        eastPanel.add(searchTextField);
    }

    protected void addSearchResultLabels(JPanel eastPanel) {
        labelSearchFound = new JLabel("  0");
        labelSearchFound.setForeground(Color.gray);
        eastPanel.add(labelSearchFound);

        JLabel labelSlash = new JLabel("/");
        labelSlash.setForeground(Color.gray);
        eastPanel.add(labelSlash);

        labelSearchTotal = new JLabel("  0");
        labelSearchTotal.setForeground(Color.gray);
        eastPanel.add(labelSearchTotal);

        JLabel labelSeparator = new JLabel("  |  ");
        labelSeparator.setForeground(Color.gray);
        eastPanel.add(labelSeparator);

    }

    protected void addSearchControlButtons(JPanel eastPanel) {
        SwingUtil swingUtil = new SwingUtil();
        IconFontSwing.register(FontAwesome.getIconFont());
        Icon iconPrevious = IconFontSwing.buildIcon(FontAwesome.ANGLE_UP, 20, new Color(80, 80, 80));
        Icon iconNext = IconFontSwing.buildIcon(FontAwesome.ANGLE_DOWN, 20, new Color(80, 80, 80));
        Icon iconClose = IconFontSwing.buildIcon(FontAwesome.TIMES, 16, new Color(80, 80, 80));

        JButton btnPrevious = swingUtil.createTransparentButton(iconPrevious, messages.getPrevious());
        btnPrevious.addActionListener(new SearchPreviousActionListener(this, datamodel));
        eastPanel.add(btnPrevious);

        JButton btnNext = swingUtil.createTransparentButton(iconNext, messages.getNext());
        btnNext.addActionListener(new SearchNextActionListener(this, datamodel));
        eastPanel.add(btnNext);

        JLabel separator = new JLabel("  ");
        eastPanel.add(separator);

        JButton btnClose = swingUtil.createTransparentButton(iconClose, messages.getCloseSearchBar());
        btnClose.addActionListener(new CloseActionListener(this));
        eastPanel.add(btnClose);
    }

    protected void addSearchAdapter() {
        SearchAdapter searchAdapter = new SearchAdapter(this, datamodel);
        searchTextField.addKeyListener(searchAdapter);
        searchTextField.addFocusListener(searchAdapter);
        datamodel.addSearchAdapter(searchAdapter);
    }

    public SearchMonitor getSearchMonitor() {
        return searchMonitor;
    }

    public void close() {
        datamodel.resetTextAttributesOfSelectedComponent();
        setVisible(false);
    }

    public synchronized void setLabelSearchFound(String text) {
        labelSearchFound.setText(text);
    }

    public synchronized void setLabelSearchTotal(String text) {
        labelSearchTotal.setText(text);
    }

    public synchronized void requestFocus() {
        searchTextField.requestFocusInWindow();
    }

    public synchronized String getSearchText() {
        return searchTextField.getText();
    }

    public synchronized void selectSearchText() {
        searchTextField.setSelectionStart(0);
        searchTextField.setSelectionEnd(searchTextField.getText().length());
    }

    public void clearSearchText() {
        searchTextField.setText("");
    }
}

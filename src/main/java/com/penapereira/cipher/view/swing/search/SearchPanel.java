package com.penapereira.cipher.view.swing.search;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.conf.SearchConfiguration;
import com.penapereira.cipher.shared.StringUtil;
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
    private StringUtil util;
    private SearchConfiguration conf;

    public SearchPanel(Messages messages, SwingDatamodelInterface datamodel, SearchConfiguration searchConf) {
        super();
        this.messages = messages;
        this.datamodel = datamodel;
        this.searchMonitor = new SearchMonitor();
        this.util = new StringUtil();
        this.conf = searchConf;

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
        labelSearchFound = new JLabel();
        labelSearchFound.setForeground(Color.gray);
        eastPanel.add(labelSearchFound);

        JLabel labelSlash = new JLabel("/");
        labelSlash.setForeground(Color.gray);
        eastPanel.add(labelSlash);

        labelSearchTotal = new JLabel();
        labelSearchTotal.setForeground(Color.gray);
        eastPanel.add(labelSearchTotal);

        JLabel labelSeparator = new JLabel("  |  ");
        labelSeparator.setForeground(Color.gray);
        eastPanel.add(labelSeparator);

        resetLabels();
    }

    protected void addSearchControlButtons(JPanel eastPanel) {
        IconFontSwing.register(FontAwesome.getIconFont());
        Color gray = new Color(conf.getIconColorR(), conf.getIconColorG(), conf.getIconColorB());
        Icon iconPrevious = IconFontSwing.buildIcon(FontAwesome.ANGLE_UP, conf.getArrowSize(), gray);
        Icon iconNext = IconFontSwing.buildIcon(FontAwesome.ANGLE_DOWN, conf.getArrowSize(), gray);
        Icon iconClose = IconFontSwing.buildIcon(FontAwesome.TIMES, conf.getCloseSize(), gray);

        SwingUtil swingUtil = new SwingUtil();
        JButton btnPrevious = swingUtil.createTransparentButton(iconPrevious, messages.getPrevious());
        btnPrevious.addActionListener(new SearchPreviousActionListener(this));
        eastPanel.add(btnPrevious);

        JButton btnNext = swingUtil.createTransparentButton(iconNext, messages.getNext());
        btnNext.addActionListener(new SearchNextActionListener(this));
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

    public synchronized SearchMonitor getSearchMonitor() {
        return searchMonitor;
    }

    public synchronized void close() {
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

    public synchronized void clearSearchText() {
        searchTextField.setText("");
    }

    protected synchronized void renderCurrentIndex() {
        synchronized (this.getTreeLock()) {
            setLabelSearchFound(util.padLeft("" + (searchMonitor.getCurrentIndex() + 1), conf.getPaddingSize(), ' '));
        }
    }

    public synchronized void renderNext() {
        if (searchMonitor.getMatches() != 0) {
            datamodel.clearMarkedText(searchMonitor.getCurrent());
            datamodel.markText(searchMonitor.getNext());
            renderCurrentIndex();
        }
    }

    public synchronized void renderPrevious() {
        if (searchMonitor.getMatches() != 0) {
            datamodel.clearMarkedText(searchMonitor.getCurrent());
            datamodel.markText(searchMonitor.getPrevious());
            renderCurrentIndex();
        }
    }

    public synchronized void resetLabels() {
        synchronized (this.getTreeLock()) {
            setLabelSearchFound("   0");
            setLabelSearchTotal("   0");
        }
    }
}

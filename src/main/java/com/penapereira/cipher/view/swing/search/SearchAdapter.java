package com.penapereira.cipher.view.swing.search;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.view.swing.datamodel.SwingDatamodelInterface;

public class SearchAdapter implements KeyListener, ChangeListener, FocusListener {

    private final Logger log = LoggerFactory.getLogger(getClass());
    protected JTextField searchTextField;
    protected String previousSearch;
    protected SwingDatamodelInterface datamodel;
    private SearchPanel searchPanel;

    public SearchAdapter(SearchPanel searchPanel, SwingDatamodelInterface datamodel) {
        this.searchTextField = searchPanel.getSearchTextField();
        this.searchPanel = searchPanel;
        this.previousSearch = "";
        this.datamodel = datamodel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        search();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        search();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        search();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (searchPanel.isVisible() && !searchTextField.getText().equals("")) {
            boolean isSearchForced = true;
            search(isSearchForced);
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (!searchTextField.getText().equals("")) {
            boolean isSearchForced = true;
            search(isSearchForced);
            searchTextField.setSelectionStart(0);
            searchTextField.setSelectionEnd(searchTextField.getText().length());
        }
    }

    @Override
    public void focusLost(FocusEvent e) {}

    protected void search() {
        if (!searchTextField.getText().equals("")) {
            search(false);
        }
    }

    protected void search(boolean force) {
        if (force || !searchTextField.getText().equals(previousSearch)) {
            try {
                performSearch();
            } catch (IOException e) {
                log.error("Could not read text from document, clearing search query");
                searchTextField.setText("");
            }
        }
    }

    protected void performSearch() throws IOException {
        String query = searchTextField.getText().toLowerCase();
        log.trace("Searching for {}", query);
        String text = datamodel.getTextFromComponent(datamodel.getSelectedComponent());
        int matches = searchPanel.getSearchMonitor().search(text, query);
        log.trace("{} matches found", matches);
        previousSearch = query;
        searchPanel.getLabelSearchFound().setText("0");
        searchPanel.getLabelSearchTotal().setText("" + matches);
    }
}

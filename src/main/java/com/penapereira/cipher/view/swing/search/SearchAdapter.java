package com.penapereira.cipher.view.swing.search;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchAdapter implements KeyListener {

    private final Logger log = LoggerFactory.getLogger(getClass());
    protected JTextField searchTextField;
    protected String previousSearch;

    public SearchAdapter(JTextField searchTextField) {
        this.searchTextField = searchTextField;
        this.previousSearch = "";
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

    protected void search() {
        if (isValidQuery()) {
            String query = searchTextField.getText();
            log.trace("Searching for {}", query);

            previousSearch = query;
        }
    }

    protected boolean isValidQuery() {
        return !searchTextField.getText().equals(previousSearch);
    }

}

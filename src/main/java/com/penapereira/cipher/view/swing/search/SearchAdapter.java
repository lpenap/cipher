package com.penapereira.cipher.view.swing.search;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.view.swing.datamodel.DatamodelInterface;

public class SearchAdapter<P> implements KeyListener, ChangeListener {

    private final Logger log = LoggerFactory.getLogger(getClass());
    protected JTextField searchTextField;
    protected String previousSearch;
    protected DatamodelInterface<P, JScrollPane, JTextPane> datamodel;

    public SearchAdapter(JTextField searchTextField, DatamodelInterface<P, JScrollPane, JTextPane> datamodel) {
        this.searchTextField = searchTextField;
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
        performSearch();
    }

    protected void search() {
        if (isValidQuery()) {
            performSearch();
        }
    }

    protected void performSearch() {
        String query = searchTextField.getText();
        log.trace("Searching for {}", query);

        previousSearch = query;
    }

    protected boolean isValidQuery() {
        return !searchTextField.getText().equals(previousSearch);
    }

}

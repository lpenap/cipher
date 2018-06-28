package com.penapereira.cipher.view.swing.search;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.stream.IntStream;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.shared.StringUtil;
import com.penapereira.cipher.view.swing.datamodel.SwingDatamodelInterface;

public class SearchAdapter implements KeyListener, ChangeListener, FocusListener {

    private final Logger log = LoggerFactory.getLogger(getClass());
    protected SwingDatamodelInterface datamodel;
    private SearchPanel searchPanel;
    private Timer searchTimer;
    private int[] ignoredKeys;
    private StringUtil util;

    public SearchAdapter(SearchPanel searchPanel, SwingDatamodelInterface datamodel) {
        this.searchPanel = searchPanel;
        this.datamodel = datamodel;
        this.searchTimer = new Timer();
        this.ignoredKeys = new int[] {KeyEvent.VK_UNDEFINED, KeyEvent.VK_ENTER};
        this.util = new StringUtil();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        log.trace("Key typed {}", e.getKeyCode());
        validateKeyAndSearch(e.getKeyCode());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        log.trace("Key pressed {}", e.getKeyCode());
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                if (searchPanel.getSearchMonitor().getMatches() != 0) {
                    log.trace("ENTER pressed, showing next search result");
                    searchPanel.renderNext();
                }
                break;
            default:
                validateInputAndPerformSearch();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        log.trace("Key released {}", e.getKeyCode());

        validateKeyAndSearch(e.getKeyCode());
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        log.trace("Tab changed");
        if (searchPanel.isVisible()) {
            validateInputAndPerformSearch();
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        log.trace("Search input text gained focus");
        validateInputAndPerformSearch();
        searchPanel.selectSearchText();
    }

    @Override
    public void focusLost(FocusEvent e) {
        datamodel.resetTextAttributesOfSelectedComponent();
    }

    protected void validateKeyAndSearch(int key) {
        boolean isKeyIgnored = IntStream.of(ignoredKeys).anyMatch(x -> x == key);
        if (!isKeyIgnored) {
            log.trace("Key {} not ignored, searching", key);
            validateInputAndPerformSearch();
        } else {
            log.trace("Key {} ignored", key);
        }
    }

    protected void validateInputAndPerformSearch() {
        String queryString = util.sanitizeString(searchPanel.getSearchText());
        if (queryString.isEmpty()) {
            clearSearch();
        } else {
            performSearch();
        }
    }

    protected void performSearch() {
        log.trace("Canceling previous tasks and launching a new SearchTask");
        searchPanel.resetLabels();
        resetTimer();
        searchTimer.schedule(new SearchTask(searchPanel, datamodel), 300);
    }

    protected void clearSearch() {
        resetTimer();
        datamodel.resetTextAttributesOfSelectedComponent();
        searchPanel.getSearchMonitor().clearSearch();
        searchPanel.resetLabels();
    }

    protected void resetTimer() {
        searchTimer.cancel();
        searchTimer.purge();
        searchTimer = new Timer();
    }
}

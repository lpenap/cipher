package com.penapereira.cipher.view.swing.search;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.view.swing.datamodel.SwingDatamodelInterface;

public class SearchAdapter implements KeyListener, ChangeListener, FocusListener {

    private final Logger log = LoggerFactory.getLogger(getClass());
    protected SwingDatamodelInterface datamodel;
    private SearchPanel searchPanel;
    private Timer searchTimer;

    public SearchAdapter(SearchPanel searchPanel, SwingDatamodelInterface datamodel) {
        this.searchPanel = searchPanel;
        this.datamodel = datamodel;
        this.searchTimer = new Timer();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        checkActionFromKey(e.getKeyCode());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        checkActionFromKey(e.getKeyCode());

    }

    @Override
    public void keyReleased(KeyEvent e) {
        checkActionFromKey(e.getKeyCode());
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (searchPanel.isVisible()) {
            validateAndPerformSearch();
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        validateAndPerformSearch();
        searchPanel.selectSearchText();
    }

    @Override
    public void focusLost(FocusEvent e) {
        datamodel.resetTextAttributesOfSelectedComponent();
    }

    protected void checkActionFromKey(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_ENTER:
                if (searchPanel.getSearchMonitor().getMatches() != 0) {
                    searchPanel.renderNext();
                }
                break;
            default:
                validateAndPerformSearch();
        }
    }

    protected void validateAndPerformSearch() {
        if (searchPanel.getSearchText().equals("")) {
            clearSearch();
        } else {
            performSearch();
        }
    }

    protected void performSearch() {
        log.trace("Launching a new SearchTask");
        searchPanel.resetLabels();
        resetTimer();
        searchTimer.schedule(new SearchTask(searchPanel, datamodel), 5000);
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

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
import com.penapereira.cipher.util.StringUtil;
import com.penapereira.cipher.view.swing.datamodel.SwingDataModelInterface;

public class SearchAdapter implements KeyListener, ChangeListener, FocusListener {

    private final Logger log = LoggerFactory.getLogger(getClass());
    protected final SwingDataModelInterface dataModel;
    private final SearchPanel searchPanel;
    private Timer searchTimer;
    private final StringUtil util;
    private final int[] ignoredKeys;

    public SearchAdapter(SearchPanel searchPanel, SwingDataModelInterface dataModel) {
        this.searchPanel = searchPanel;
        this.dataModel = dataModel;
        this.searchTimer = new Timer();
        this.util = new StringUtil();
        this.ignoredKeys = new int[] {KeyEvent.VK_UNDEFINED, KeyEvent.VK_ENTER, KeyEvent.VK_SHIFT};
    }

    @Override
    public void keyTyped(KeyEvent e) {
        validateKeyAndSearch(e, "key typed");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            performNextPreviousEvent(e);
        } else {
            validateKeyAndSearch(e, "key pressed");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        validateKeyAndSearch(e, "key released");
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        log.trace("Tab changed");
        if (searchPanel.isVisible()) {
            clearSearch();
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
        dataModel.resetTextAttributesOfSelectedComponent();
    }

    protected void validateKeyAndSearch(KeyEvent event, String traceMessage) {
        log.trace("{} '{}' with modifiers '{}'", traceMessage, event.getKeyCode(),
                KeyEvent.getKeyModifiersText(event.getModifiers()));
        boolean isKeyIgnored = IntStream.of(ignoredKeys).anyMatch(x -> x == event.getKeyCode());
        if (!isKeyIgnored) {
            validateInputAndPerformSearch();
        }
    }

    protected void performNextPreviousEvent(KeyEvent event) {
        int onMaskPrevious = KeyEvent.SHIFT_DOWN_MASK;
        int offMaskPrevious = KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK;

        if ((event.getModifiersEx() & (onMaskPrevious | offMaskPrevious)) == onMaskPrevious) {
            log.debug("Rendering PREV search result");
            searchPanel.renderPrevious();
        } else {
            log.debug("Rendering NEXT search result");
            searchPanel.renderNext();
        }
    }

    protected void validateInputAndPerformSearch() {
        String dirtyString = searchPanel.getSearchText();
        String queryString = util.sanitizeString(dirtyString);
        log.trace("Sanitized query string: '{}' . Dirty: '{}'", queryString, dirtyString);
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
        searchTimer.schedule(new SearchTask(searchPanel, dataModel), 300);
    }

    protected void clearSearch() {
        resetTimer();
        dataModel.resetTextAttributesOfSelectedComponent();
        searchPanel.getSearchMonitor().clearSearch();
        searchPanel.resetLabels();
    }

    protected void resetTimer() {
        searchTimer.cancel();
        searchTimer.purge();
        searchTimer = new Timer();
    }
}

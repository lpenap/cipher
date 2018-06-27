package com.penapereira.cipher.view.swing.search;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import com.penapereira.cipher.shared.StringUtil;
import com.penapereira.cipher.view.swing.datamodel.SwingDatamodelInterface;

public class SearchAdapter implements KeyListener, ChangeListener, FocusListener {

    private final Logger log = LoggerFactory.getLogger(getClass());
    protected String previousSearch;
    protected SwingDatamodelInterface datamodel;
    private SearchPanel searchPanel;
    private StringUtil util;

    public SearchAdapter(SearchPanel searchPanel, SwingDatamodelInterface datamodel) {
        this.searchPanel = searchPanel;
        this.previousSearch = "";
        this.datamodel = datamodel;
        this.util = new StringUtil();
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
        if (searchPanel.isVisible() && !searchPanel.getSearchText().equals("")) {
            search();
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (!searchPanel.getSearchText().equals("")) {
            search();
            searchPanel.selectSearchText();
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        datamodel.resetTextAttributesOfSelectedComponent();
    }

    protected void search() {
        if (searchPanel.getSearchText().equals("")) {
            clearSearch();
        } else {
            try {
                performSearch();
            } catch (IOException e) {
                log.error("Could not read text from document, clearing search query");
                searchPanel.clearSearchText();
            }
        }
    }

    protected void performSearch() throws IOException {
        String query = searchPanel.getSearchText().toLowerCase();
        log.trace("Searching for {}", query);
        String text = datamodel.getTextFromComponent(datamodel.getSelectedComponent());
        int matches = searchPanel.getSearchMonitor().search(text, query);
        log.trace("{} matches found", matches);
        previousSearch = query;
        searchPanel.setLabelSearchTotal(util.padLeft("" + matches, 3, ' '));
        selectFirst();
    }

    protected void clearSearch() {
        datamodel.resetTextAttributesOfSelectedComponent();
        searchPanel.getSearchMonitor().clearSearch();
        resetLabels();
    }

    protected void resetLabels() {
        searchPanel.setLabelSearchFound("  0");
        searchPanel.setLabelSearchTotal("  0");
    }

    protected void selectFirst() {
        datamodel.resetTextAttributesOfSelectedComponent();
        SearchMonitor searchMonitor = searchPanel.getSearchMonitor();
        if (searchMonitor.getMatches() != 0) {
            searchPanel.renderCurrentIndex();
            Pair<Integer, Integer> indexes = searchMonitor.getCurrent();
            log.trace("Selecting first search result " + indexes.toString());
            datamodel.markText(indexes);
        } else {
            resetLabels();
        }
    }
}

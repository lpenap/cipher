package com.penapereira.cipher.view.swing.search;

import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import com.penapereira.cipher.shared.StringUtil;
import com.penapereira.cipher.view.swing.datamodel.SwingDataModelInterface;

public class SearchTask extends TimerTask {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final SearchPanel searchPanel;
    private final SwingDataModelInterface dataModel;
    private final StringUtil util;

    public SearchTask(SearchPanel searchPanel, SwingDataModelInterface dataModel) {
        this.searchPanel = searchPanel;
        this.dataModel = dataModel;
        this.util = new StringUtil();
        searchPanel.getSearchMonitor().setTimerRunning(true);
    }

    @Override
    public void run() {
        String query = searchPanel.getSearchText().toLowerCase();
        log.trace("Searching for {}", query);
        String text = dataModel.getTextFromComponent(dataModel.getSelectedComponent());
        int matches = searchPanel.getSearchMonitor().search(text, query);
        log.trace("{} matches found", matches);
        synchronized (searchPanel.getTreeLock()) {
            searchPanel.setLabelSearchTotal(util.padLeft("" + matches, 3, ' '));
            selectFirst();
        }
    }

    protected void selectFirst() {
        dataModel.resetTextAttributesOfSelectedComponent();
        SearchMonitor searchMonitor = searchPanel.getSearchMonitor();
        if (searchMonitor.getMatches() != 0) {
            searchPanel.renderCurrentIndex();
            Pair<Integer, Integer> indexes = searchMonitor.getCurrent();
            log.trace("Selecting first search result " + indexes.toString());
            dataModel.markText(indexes);
        }
    }

}

package com.penapereira.cipher.view.swing.search;

import java.awt.event.ActionListener;
import com.penapereira.cipher.shared.StringUtil;
import com.penapereira.cipher.view.swing.datamodel.SwingDatamodelInterface;
import lombok.Data;

@Data
public abstract class AbstractSearchActionListener implements ActionListener {

    protected SearchPanel searchPanel;
    protected SwingDatamodelInterface datamodel;
    protected SearchMonitor searchMonitor;
    protected StringUtil util;

    public AbstractSearchActionListener(SearchPanel searchPanel, SwingDatamodelInterface datamodel) {
        this.searchPanel = searchPanel;
        this.datamodel = datamodel;
        this.searchMonitor = searchPanel.getSearchMonitor();
        this.util = new StringUtil();
    }

    protected void renderCurrentIndex() {
        searchPanel.setLabelSearchFound(util.padLeft("" + (searchMonitor.getCurrentIndex() + 1), 3, ' '));
    }
}

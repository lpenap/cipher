package com.penapereira.cipher.view.swing.search;

import java.awt.event.ActionEvent;
import com.penapereira.cipher.view.swing.datamodel.SwingDatamodelInterface;

public class SearchNextActionListener extends AbstractSearchActionListener {

    public SearchNextActionListener(SearchPanel searchPanel, SwingDatamodelInterface datamodel) {
        super(searchPanel, datamodel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        datamodel.clearMarkedText(searchMonitor.getCurrent());
        datamodel.markText(searchMonitor.getNext());
        renderCurrentIndex();
    }
}

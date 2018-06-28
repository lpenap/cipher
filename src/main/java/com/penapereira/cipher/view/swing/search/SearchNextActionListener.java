package com.penapereira.cipher.view.swing.search;

import java.awt.event.ActionEvent;

public class SearchNextActionListener extends AbstractSearchActionListener {

    public SearchNextActionListener(SearchPanel searchPanel) {
        super(searchPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (getSearchPanel().getSearchMonitor().getMatches() != 0) {
            getSearchPanel().renderNext();
        }
    }
}

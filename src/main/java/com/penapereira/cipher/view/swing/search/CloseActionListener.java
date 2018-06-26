package com.penapereira.cipher.view.swing.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CloseActionListener implements ActionListener {

    private SearchPanel searchPanel;

    public CloseActionListener(SearchPanel searchPanel) {
        this.searchPanel = searchPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        searchPanel.setVisible(false);
    }

}

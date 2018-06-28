package com.penapereira.cipher.view.swing.search;

import java.awt.event.ActionListener;
import lombok.Data;

@Data
public abstract class AbstractSearchActionListener implements ActionListener {

    protected SearchPanel searchPanel;

    public AbstractSearchActionListener(SearchPanel searchPanel) {
        this.searchPanel = searchPanel;
    }

}

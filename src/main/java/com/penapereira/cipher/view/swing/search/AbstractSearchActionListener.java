package com.penapereira.cipher.view.swing.search;

import java.awt.event.ActionListener;
import com.penapereira.cipher.view.swing.datamodel.SwingDatamodelInterface;
import lombok.Data;

@Data
public abstract class AbstractSearchActionListener implements ActionListener {

    protected SearchPanel searchPanel;
    protected SwingDatamodelInterface datamodel;

    public AbstractSearchActionListener(SearchPanel searchPanel, SwingDatamodelInterface datamodel) {
        this.searchPanel = searchPanel;
        this.datamodel = datamodel;
    }
}

package com.penapereira.cipher.view.swing.search;

import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import com.penapereira.cipher.view.swing.datamodel.DatamodelInterface;

public abstract class AbstractSearchActionListener<P> implements ActionListener {

    protected SearchPanel<P> searchPanel;
    protected DatamodelInterface<P, JScrollPane, JTextPane> datamodel;

    public AbstractSearchActionListener(SearchPanel<P> searchPanel,
            DatamodelInterface<P, JScrollPane, JTextPane> datamodel) {

        this.searchPanel = searchPanel;
        this.datamodel = datamodel;
    }
}

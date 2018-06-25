package com.penapereira.cipher.view.swing.search;

import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.view.swing.datamodel.DatamodelInterface;

public class SearchNextActionListener<P> extends AbstractSearchActionListener<P> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public SearchNextActionListener(SearchPanel<P> searchPanel,
            DatamodelInterface<P, JScrollPane, JTextPane> datamodel) {
        super(searchPanel, datamodel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        log.debug("Search next");
    }

}

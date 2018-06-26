package com.penapereira.cipher.view.swing.search;

import java.awt.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.view.swing.datamodel.SwingDatamodelInterface;

public class SearchNextActionListener extends AbstractSearchActionListener {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public SearchNextActionListener(SearchPanel searchPanel, SwingDatamodelInterface datamodel) {
        super(searchPanel, datamodel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        log.debug("Search next");
    }

}

package com.penapereira.cipher.view.swing;

import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import com.penapereira.cipher.view.swing.datamodel.SwingDatamodelInterface;
import com.penapereira.cipher.view.swing.datamodel.impl.TabbedPaneDatamodel;
import com.penapereira.cipher.view.swing.search.SearchPanel;

@Component
public class TabbedPaneUserInterface extends AbstractSwingInterface {

    private static final long serialVersionUID = 1L;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final String uiName = "TabbedPane Swing UI";

    JTabbedPane tabbedPane;

    @Autowired
    public TabbedPaneUserInterface(ApplicationContext context) {
        super(context);
    }

    @Override
    protected SwingDatamodelInterface buildDatamodel(ApplicationContext context) {
        return context.getBean(TabbedPaneDatamodel.class);
    }

    @Override
    protected void build(ApplicationContext context) {
        MainMenuBuilder menuBuilder = context.getBean(MainMenuBuilder.class);
        menuBuilder.setParentFrame(this);
        setJMenuBar(menuBuilder.buildJMenuBar());
        tabbedPane = (JTabbedPane) getDatamodel().getMainComponent();
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    @Override
    protected void displayAllDocuments() {
        log.debug("Refreshing all documents");
        getDatamodel().setDocuments(getDocumentController().getAll());
        tabbedPane = (JTabbedPane) getDatamodel().getMainComponent();
        if (tabbedPane.getTabCount() > 0) {
            tabbedPane.setSelectedIndex(0);
        }
    }

    @Override
    public String getUserInterfaceName() {
        return uiName;
    }

    @Override
    protected SearchPanel buildSearchPanel() {
        return new SearchPanel(messages, datamodel);
    }

}

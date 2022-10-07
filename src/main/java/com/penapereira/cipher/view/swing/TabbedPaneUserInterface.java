package com.penapereira.cipher.view.swing;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import javax.swing.JTabbedPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import com.penapereira.cipher.conf.SearchConfiguration;
import com.penapereira.cipher.view.swing.datamodel.SwingDataModelInterface;
import com.penapereira.cipher.view.swing.datamodel.impl.TabbedPaneDataModel;
import com.penapereira.cipher.view.swing.search.SearchPanel;

@Component
public class TabbedPaneUserInterface extends AbstractSwingInterface {

    private static final long serialVersionUID = 1L;
    private final Logger log = LoggerFactory.getLogger(getClass());

    JTabbedPane tabbedPane;

    @Autowired
    public TabbedPaneUserInterface(ApplicationContext context) {
        super(context);
    }

    @Override
    protected SwingDataModelInterface buildDataModel(ApplicationContext context) {
        return context.getBean(TabbedPaneDataModel.class);
    }

    @Override
    protected void build(ApplicationContext context) {
        MainMenuBuilder menuBuilder = context.getBean(MainMenuBuilder.class);
        menuBuilder.setParentFrame(this);
        setJMenuBar(menuBuilder.buildJMenuBar());
        tabbedPane = (JTabbedPane) getDataModel().getMainComponent();
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    @Override
    protected void displayAllDocuments() {
        log.debug("Refreshing all documents");
        getDataModel().setDocuments(getDocumentController().getAll());
        tabbedPane = (JTabbedPane) getDataModel().getMainComponent();
        if (tabbedPane.getTabCount() > 0) {
            tabbedPane.setSelectedIndex(0);
        }
    }

    @Override
    public String getUserInterfaceName() {
        return "TabbedPane Swing UI";
    }

    @Override
    protected SearchPanel buildSearchPanel(ApplicationContext context) {
        return new SearchPanel(messages, dataModel, context.getBean(SearchConfiguration.class));
    }
}

package com.penapereira.cipher.view.swing;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import com.penapereira.cipher.view.swing.datamodel.DatamodelInterface;
import com.penapereira.cipher.view.swing.datamodel.TabbedPaneDatamodel;

@Component
public class TabbedPaneUserInterface extends AbstractSwingInterface<JTabbedPane> {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(TabbedPaneUserInterface.class);
    private final String uiName = "TabbedPane Swing UI";

    protected MainMenuBuilder menuBuilder;
    protected JTabbedPane documentsTabbedPane;

    public TabbedPaneUserInterface(ApplicationContext context) {
        super(context);
    }

    @Override
    protected void build(ApplicationContext context) {
        menuBuilder = context.getBean(MainMenuBuilder.class);
        menuBuilder.setMainUserInterface(this);
        setJMenuBar(menuBuilder.buildJMenuBar());
        documentsTabbedPane = getDatamodel().getWrappedDatamodel();
        getContentPane().add(documentsTabbedPane, BorderLayout.CENTER);
    }

    @Override
    protected DatamodelInterface<JTabbedPane, JScrollPane, JTextPane> createDatamodel() {
        return new TabbedPaneDatamodel();
    }

    public JTabbedPane getDocumentsPane() {
        return documentsTabbedPane;
    }

    @Override
    public void launch() {
        log.debug("Launching user interface...");
        this.setVisible(true);
    }

    @Override
    protected void displayAllDocuments() {
        log.debug("Refreshing all documents");
        getDatamodel().setDocuments(getDocumentController().getAll());
        documentsTabbedPane = getDatamodel().getWrappedDatamodel();
    }

    @Override
    public String getUserInterfaceName() {
        return uiName;
    }

}

package com.penapereira.cipher.view.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;

import com.penapereira.cipher.controller.ActionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import com.penapereira.cipher.conf.Configuration;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentController;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.util.SwingUtil;
import com.penapereira.cipher.view.MainUserInterface;
import com.penapereira.cipher.view.swing.datamodel.SwingDataModelInterface;
import com.penapereira.cipher.view.swing.listener.WindowExitListener;
import com.penapereira.cipher.view.swing.search.SearchPanel;
import com.penapereira.cipher.view.swing.search.SearchPanelDispatcher;

public abstract class AbstractSwingInterface extends JFrame implements MainUserInterface, PropertyChangeListener {

    private static final long serialVersionUID = 1L;
    private final Logger log = LoggerFactory.getLogger(AbstractSwingInterface.class);
    protected final DocumentController documentController;
    protected final Messages messages;
    protected final Configuration config;
    protected final SwingDataModelInterface dataModel;
    protected SearchPanel searchPanel;

    @Autowired
    ActionType actionType;

    @Autowired
    public AbstractSwingInterface(ApplicationContext context) {
        super();

        documentController = context.getBean(DocumentController.class);
        messages = context.getBean(Messages.class);
        config = context.getBean(Configuration.class);
        dataModel = buildDataModel(context);

        setTitle(messages.getWindowTitle());
        addSearchPanel(context);
        setSize();
        addWindowListener(new WindowExitListener());
        build(context);
    }

    protected abstract void displayAllDocuments();

    protected abstract SwingDataModelInterface buildDataModel(ApplicationContext context);

    protected abstract void build(ApplicationContext context);

    protected abstract SearchPanel buildSearchPanel(ApplicationContext context);

    protected void addSearchPanel(ApplicationContext context) {
        searchPanel = buildSearchPanel(context);
        getContentPane().add(searchPanel, BorderLayout.NORTH);
    }

    @Override
    public boolean init() {
        dataModel
                .setDocumentContainerFont(new Font(config.getDocumentFont(), Font.PLAIN, config.getDocumentFontSize()));
        List<Document> documents = documentController.getAll();
        log.debug("Initializing main user interface...");
        boolean isInitCompleted = true;

        if (documents.isEmpty()) {
            if (new SwingUtil(this).confirm(messages.getSetupConfirmTitle(), messages.getSetupConfirmMsg())) {
                initializeWelcomeDocument();
            } else {
                log.info("User didn't want to continue. Exiting...");
                isInitCompleted = false;
            }
        } else {
            log.debug("Documents repository has {} document(s), proceeding to render them...", documents.size());
            displayAllDocuments();
        }
        return isInitCompleted;
    }

    @Override
    public void launch() {
        log.debug("Launching user interface...");
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new SearchPanelDispatcher(searchPanel));

        log.debug("Registering swing UI as property change listener in the document controller...");
        documentController.addPropertyChangeListener(this);

        this.setVisible(true);
    }

    private void updateDocument(Document doc) {
        log.debug("*UPDATE* document '{}' ", doc.getTitle());
        dataModel.updateDocument(doc);
    }

    private void deleteDocument(Document doc) {
        log.debug("*DELETE* document '{}' ", doc.getTitle());
        dataModel.deleteDocument(doc);
    }

    private void addDocument(Document doc) {
        log.debug("*ADD* document '{}' ", doc.getTitle());
        dataModel.addDocument(doc);
    }

    private void initializeWelcomeDocument() {
        log.debug("Initializing document repository with welcome document!");
        Document helpDocument = documentController.getHelpDocument();
        documentController.createAndSaveDocument(helpDocument.getTitle(), helpDocument.getText());
    }

    protected void setSize() {
        setResizable(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = Math.min(config.getWindowWidth(), (int) screenSize.getWidth() / 2);
        setSize(width, (int) screenSize.getHeight() - 100);
        // setSize(500, 500);
    }

    public SwingDataModelInterface getDataModel() {
        return dataModel;
    }

    protected DocumentController getDocumentController() {
        return documentController;
    }


    /**
     * propertyChange method from Observer/Listener pattern invoked every time a document is modified (created,
     * updated, deleted)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String actionName = evt.getPropertyName();
        log.debug("Received event \"" + actionName + "\"" );
        if (actionType.ADD.equals(actionName)) {
            addDocument((Document) evt.getNewValue());
        }
        if (actionType.DELETE.equals(actionName)) {
            deleteDocument((Document) evt.getOldValue());
        }
        if (actionType.UDPATE.equals(actionName)) {
            updateDocument((Document) evt.getNewValue());
        }
        if (actionType.UPDATE_ALL.equals(actionName)) {
            displayAllDocuments();
        }
    }
}

package com.penapereira.cipher.view.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import com.penapereira.cipher.conf.Configuration;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentActionInterface;
import com.penapereira.cipher.controller.DocumentController;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.shared.SwingUtil;
import com.penapereira.cipher.view.MainUserInterface;
import com.penapereira.cipher.view.swing.datamodel.DatamodelInterface;
import com.penapereira.cipher.view.swing.listener.WindowExitListener;
import com.penapereira.cipher.view.swing.search.SearchPanel;
import com.penapereira.cipher.view.swing.search.SearchPanelDispatcher;

public abstract class AbstractSwingInterface<P> extends JFrame implements MainUserInterface, Observer {

    private static final long serialVersionUID = 1L;

    private final Logger log = LoggerFactory.getLogger(getClass());

    protected DocumentController documentController;
    protected Messages messages;
    protected Configuration config;

    protected DatamodelInterface<P, JScrollPane, JTextPane> datamodel;
    private SearchPanel<P> searchPanel;

    @Autowired
    public AbstractSwingInterface(ApplicationContext context) {
        super();

        documentController = context.getBean(DocumentController.class);
        messages = context.getBean(Messages.class);
        config = context.getBean(Configuration.class);

        datamodel = createDatamodel();
        setTitle(messages.getWindowTitle());
        addSearchPanel();
        setSize();
        addWindowListener(new WindowExitListener());
        build(context);
    }

    protected abstract void build(ApplicationContext context);

    protected abstract DatamodelInterface<P, JScrollPane, JTextPane> createDatamodel();

    protected abstract void displayAllDocuments();

    protected abstract P getDocumentsPane();

    protected void addSearchPanel() {
        searchPanel = new SearchPanel<P>(messages, datamodel);
        getContentPane().add(searchPanel, BorderLayout.NORTH);
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new SearchPanelDispatcher<P>(searchPanel));
    }

    @Override
    public boolean init() {
        datamodel
                .setDocumentContainerFont(new Font(config.getDocumentFont(), Font.PLAIN, config.getDocumentFontSize()));
        documentController.addObserver(this);
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
        this.setVisible(true);
    }

    /**
     * Update method from Observer pattern invoked every time a document is modified (created, updated, deleted)
     */
    @Override
    public void update(Observable o, Object arg) {
        log.debug("Update request received from controller");
        if (arg != null && arg instanceof DocumentActionInterface) {
            DocumentActionInterface action = (DocumentActionInterface) arg;
            Document doc = action.getDocument();
            switch (action.getAction()) {
                case ADD:
                    addDocument(doc);
                    break;
                case DELETE:
                    deleteDocument(doc);
                    break;
                case UPDATE:
                    updateDocument(doc);
                    break;
            }
        } else {
            displayAllDocuments();
        }
    }

    private void updateDocument(Document doc) {
        log.debug("*UPDATE* document '{}' ", doc.getTitle());
        datamodel.updateDatamodelForDocument(doc);
    }

    private void deleteDocument(Document doc) {
        log.debug("*DELETE* document '{}' ", doc.getTitle());
        datamodel.deleteDocument(doc);
    }

    private void addDocument(Document doc) {
        log.debug("*ADD* document '{}' ", doc.getTitle());
        datamodel.addDocument(doc);
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
        //setSize(500, 500);
    }

    public DatamodelInterface<P, JScrollPane, JTextPane> getDatamodel() {
        return datamodel;
    }

    protected DocumentController getDocumentController() {
        return documentController;
    }

    public JPanel getSearchPanel() {
        return searchPanel;
    }
}

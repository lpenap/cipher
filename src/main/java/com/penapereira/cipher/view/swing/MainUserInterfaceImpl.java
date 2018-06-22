package com.penapereira.cipher.view.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import com.penapereira.cipher.conf.Configuration;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentActionInterface;
import com.penapereira.cipher.controller.DocumentController;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.shared.SwingUtil;
import com.penapereira.cipher.view.MainUserInterface;

@Component
public class MainUserInterfaceImpl extends JFrame implements MainUserInterface, Observer {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(MainUserInterfaceImpl.class);

    protected DocumentController documentController;
    protected Messages messages;
    protected Configuration config;

    protected MainMenuBuilder menuBuilder;
    protected DocumentModel documentModel;
    protected JTabbedPane documentsTabbedPane;

    @Autowired
    public MainUserInterfaceImpl(ApplicationContext context) {
        super();

        documentController = context.getBean(DocumentController.class);
        messages = context.getBean(Messages.class);
        config = context.getBean(Configuration.class);
        menuBuilder = context.getBean(MainMenuBuilder.class);

        documentModel = DocumentModel.instance();
        documentModel.setDocuments(documentController.getAll());
        documentModel.setDocumentFont(config.getDocumentFont());
        documentModel.setDocumentFontSize(config.getDocumentFontSize());

        setTitle(messages.getWindowTitle());
        setSize();
        setResizable(true);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        menuBuilder.setMainUserInterface(this);
        setJMenuBar(menuBuilder.buildJMenuBar());

        documentsTabbedPane = documentModel.getTabbedPane();
        getContentPane().add(documentsTabbedPane, BorderLayout.CENTER);
    }

    @Override
    public boolean init() {
        this.documentController.addObserver(this);
        log.debug("Initializing main user interface...");
        boolean isInitCompleted = true;

        if (documentModel.getDocuments().isEmpty()) {
            if (new SwingUtil(this).confirm(messages.getSetupConfirmTitle(), messages.getSetupConfirmMsg())) {
                initializeWelcomeDocument();
            } else {
                log.info("User didn't want to continue. Exiting...");
                isInitCompleted = false;
            }
        } else {
            log.debug("Documents repository has {} document(s), proceeding to render them...",
                    documentModel.getDocuments().size());
            displayAllDocuments();
        }
        return isInitCompleted;
    }

    private void initializeWelcomeDocument() {
        log.debug("Initializing document repository with welcome document!");
        Document helpDocument = documentController.getHelpDocument();
        documentController.save(helpDocument);
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
        log.debug("*UPDATE* document " + doc.getTitle());
        displayAllDocuments();
    }

    private void deleteDocument(Document doc) {
        log.debug("*DELETE* document " + doc.getTitle());
        displayAllDocuments();
    }

    private void addDocument(Document doc) {
        log.debug("*ADD* document " + doc.getTitle());
        displayAllDocuments();
    }

    protected void displayAllDocuments() {
        log.debug("Refreshing all documents");
        documentModel.setDocuments(documentController.getAll());
        documentsTabbedPane = documentModel.getTabbedPane();
    }

    protected void setSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = Math.min(config.getWindowWidth(), (int) screenSize.getWidth() / 2);
        setSize(width, (int) screenSize.getHeight() - 100);
        // setSize(500, 500);
    }

    @Override
    public String getUserInterfaceName() {
        return "Swing UI";
    }

    public DocumentModel getDocumentModel() {
        return documentModel;
    }
}

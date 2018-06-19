package com.penapereira.cipher.view.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
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

    protected List<Document> documents;
    private JTabbedPane documentsTabbedPane;

    protected Map<JScrollPane, Long> modelMap;
    protected MainMenuBuilder menuBuilder;

    @Autowired
    public MainUserInterfaceImpl(ApplicationContext context) {
        super();

        this.documentController = context.getBean(DocumentController.class);
        this.messages = context.getBean(Messages.class);
        this.config = context.getBean(Configuration.class);
        this.menuBuilder = context.getBean(MainMenuBuilder.class);

        this.documents = documentController.getAll();
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

        documentsTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(documentsTabbedPane, BorderLayout.CENTER);
    }

    @Override
    public boolean init() {
        this.documentController.addObserver(this);
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
        documents = documentController.getAll();
        getDocumentsTabbedPane().removeAll();
        this.modelMap = new HashMap<JScrollPane, Long>();
        Iterator<Document> i = documents.iterator();
        while (i.hasNext()) {
            Document doc = i.next();
            JTextPane textPane = new JTextPane();
            StyledDocument styledDoc = textPane.getStyledDocument();
            textPane.setFont(new Font(config.getDocumentFont(), Font.PLAIN, config.getDocumentFontSize()));
            try {
                styledDoc.insertString(styledDoc.getLength(), doc.getText(), styledDoc.getStyle("regular"));
            } catch (BadLocationException e) {
                log.error("Could not display the document '" + doc.getTitle() + "' , skipping...");
                continue;
            }
            JScrollPane scrollPane = new JScrollPane(textPane);
            modelMap.put(scrollPane, doc.getId());
            documentsTabbedPane.addTab(doc.getTitle(), scrollPane);
        }
    }

    public JTabbedPane getDocumentsTabbedPane() {
        return documentsTabbedPane;
    }

    public Long getDocumentIdFromScrollPane(JScrollPane tab) {
        return modelMap.get(tab);
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
}

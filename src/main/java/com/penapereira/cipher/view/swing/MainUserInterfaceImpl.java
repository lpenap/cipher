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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.penapereira.cipher.conf.Configuration;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentController;
import com.penapereira.cipher.controller.DocumentActionInterface;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.shared.SwingUtil;
import com.penapereira.cipher.view.MainUserInterface;
import com.penapereira.cipher.view.swing.listener.AboutActionListener;
import com.penapereira.cipher.view.swing.listener.AddDocumentActionListener;
import com.penapereira.cipher.view.swing.listener.DeleteDocumentActionListener;
import com.penapereira.cipher.view.swing.listener.ExitActionListener;

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

    @Autowired
    public MainUserInterfaceImpl(DocumentController documentController, Messages messages, Configuration config) {
        super();

        this.documentController = documentController;
        this.documents = documentController.getAll();
        this.messages = messages;
        this.config = config;

        setTitle(messages.getWindowTitle());
        setSize();
        setResizable(true);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu cipherMenu = new JMenu(messages.getCipherMenu());
        menuBar.add(cipherMenu);

        JMenuItem menuItemAbout = new JMenuItem(messages.getAboutMenu());
        menuItemAbout.addActionListener(new AboutActionListener(messages));
        cipherMenu.add(menuItemAbout);
        cipherMenu.addSeparator();

        JMenuItem menuItemExit = new JMenuItem(messages.getExitMenu());
        menuItemExit.addActionListener(new ExitActionListener());
        cipherMenu.add(menuItemExit);

        JMenu documentMenu = new JMenu(messages.getDocumentMenu());
        menuBar.add(documentMenu);

        JMenuItem menuItemAddDocument = new JMenuItem(messages.getAddDocumentMenu());
        menuItemAddDocument.addActionListener(new AddDocumentActionListener(documentController, messages));
        documentMenu.add(menuItemAddDocument);

        JMenuItem menuItemSaveAll = new JMenuItem(messages.getSaveAllMenu());
        documentMenu.add(menuItemSaveAll);
        cipherMenu.addSeparator();

        JMenuItem menuItemDeleteDocument = new JMenuItem(messages.getDeleteDocumentMenu());
        menuItemDeleteDocument.addActionListener(new DeleteDocumentActionListener(documentController, messages, this));
        documentMenu.addSeparator();
        documentMenu.add(menuItemDeleteDocument);

        documentsTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(documentsTabbedPane, BorderLayout.CENTER);
    }

    @Override
    public boolean init() {
        this.documentController.addObserver(this);
        log.info("Preparing main user interface...");
        boolean isInitCompleted = true;

        if (documents.isEmpty()) {
            if (new SwingUtil(this).confirm(messages.getSetupConfirmTitle(), messages.getSetupConfirmMsg())) {
                initializeWelcomeDocument();
            } else {
                log.info("User didn't want to continue. Exiting...");
                isInitCompleted = false;
            }
        } else {
            log.info("Documents repository has {} document(s), proceeding to render them...", documents.size());
            displayAllDocuments();
        }
        displayAllDocuments();
        return isInitCompleted;
    }

    private void initializeWelcomeDocument() {
        log.info("Initializing document repository with welcome document!");
        Document helpDocument = documentController.getHelpDocument();
        documentController.save(helpDocument);
    }

    @Override
    public void launch() {
        this.setVisible(true);
    }

    /**
     * Update method from Observer pattern invoked every time a document is modified (created, updated, deleted)
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && arg instanceof DocumentActionInterface) {
            DocumentActionInterface action = (DocumentActionInterface) arg;
            Document doc = action.getDocument();
            log.info("Need to update document " + doc.getTitle());
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
            log.info("Need to update all documents");
            displayAllDocuments();
        }
    }

    private void updateDocument(Document doc) {
        displayAllDocuments();
    }

    private void deleteDocument(Document doc) {
        displayAllDocuments();
    }

    private void addDocument(Document doc) {
        displayAllDocuments();
    }

    protected void displayAllDocuments() {
        documents = documentController.getAll();
        getDocumentsTabbedPane().removeAll();
        this.modelMap = new HashMap<JScrollPane, Long>();
        Iterator<Document> i = documents.iterator();
        while (i.hasNext()) {
            Document doc = i.next();
            JTextPane textPane = new JTextPane();
            StyledDocument styledDoc = textPane.getStyledDocument();
            textPane.setFont(new Font("Courier", Font.PLAIN, 16));
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
}

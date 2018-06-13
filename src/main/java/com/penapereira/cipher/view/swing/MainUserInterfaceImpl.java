package com.penapereira.cipher.view.swing;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.List;
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
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.shared.Util;
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
        menuItemAbout.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                about();
            }
        });
        cipherMenu.add(menuItemAbout);
        cipherMenu.addSeparator();

        JMenuItem menuItemExit = new JMenuItem(messages.getExitMenu());
        menuItemExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        cipherMenu.add(menuItemExit);

        JMenu documentMenu = new JMenu(messages.getDocumentMenu());
        menuBar.add(documentMenu);

        JMenuItem menuItemAddDocument = new JMenuItem(messages.getAddDocumentMenu());
        menuItemAddDocument.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        documentMenu.add(menuItemAddDocument);

        JMenuItem menuItemSaveAll = new JMenuItem(messages.getSaveAllMenu());
        documentMenu.add(menuItemSaveAll);

        documentsTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(documentsTabbedPane, BorderLayout.CENTER);
    }

    @Override
    public boolean init() {
        this.documentController.addObserver(this);
        log.info("Preparing main user interface...");
        boolean isInitCompleted = true;

        if (documents.isEmpty()) {
            if (confirm(messages.getSetupConfirmTitle(), messages.getSetupConfirmMsg())) {
                initializeWelcomeDocument();
            } else {
                log.info("User didn't want to continue. Exiting...");
                isInitCompleted = false;
            }
        } else {
            log.info("Documents repository has {} document(s), proceeding to render them...", documents.size());
            this.update(documentController, null);
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

    protected void alert(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    protected boolean confirm(String title, String msg) {
        int dialogResult = JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.YES_NO_OPTION);
        return dialogResult == JOptionPane.YES_OPTION;
    }

    @Override
    public void about() {
        alert(new Util().listToString(messages.getAbout()));
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && arg instanceof Document) {
            Document doc = (Document) arg;
            log.info("Need to update document " + doc.getTitle());
        } else {
            log.info("Need to update all documents");
        }
        displayAllDocuments();
    }

    protected void displayAllDocuments() {
        documents = documentController.getAll();
        getDocumentsTabbedPane().removeAll();
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
            documentsTabbedPane.addTab(doc.getTitle(), scrollPane);
        }
    }

    protected JTabbedPane getDocumentsTabbedPane() {
        return documentsTabbedPane;
    }

    protected void setSize() {
        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // int width = Math.min(config.getWindowWidth(), (int) screenSize.getWidth() / 2);
        // setSize(width, (int) screenSize.getHeight() - 100);
        setSize(500, 500);
    }
}

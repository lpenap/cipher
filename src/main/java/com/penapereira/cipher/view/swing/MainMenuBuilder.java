package com.penapereira.cipher.view.swing;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentController;
import com.penapereira.cipher.view.swing.listener.AboutActionListener;
import com.penapereira.cipher.view.swing.listener.AddDocumentDelegate;
import com.penapereira.cipher.view.swing.listener.DeleteDocumentActionListener;
import com.penapereira.cipher.view.swing.listener.ExitActionListener;
import com.penapereira.cipher.view.swing.listener.RenameDocumentDelegate;
import com.penapereira.cipher.view.swing.listener.SaveAllActionListener;

@Component
public class MainMenuBuilder {

    private Messages messages;
    private DocumentController documentController;
    private TabbedPaneUserInterface parent;

    @Autowired
    public MainMenuBuilder(DocumentController documentController, Messages messages) {
        this.messages = messages;
        this.documentController = documentController;
    }

    public void setParentFrame(TabbedPaneUserInterface parent) {
        this.parent = parent;
    }

    public JMenuBar buildJMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        buildCipherMenu(menuBar);
        buildDocumentMenu(menuBar);
        return menuBar;
    }

    protected void buildCipherMenu(JMenuBar menuBar) {
        JMenu cipherMenu = new JMenu(messages.getCipherMenu());
        menuBar.add(cipherMenu);

        JMenuItem menuItemAbout = new JMenuItem(messages.getAboutMenu());
        menuItemAbout.addActionListener(new AboutActionListener(messages));
        cipherMenu.add(menuItemAbout);
        cipherMenu.addSeparator();

        JMenuItem menuItemExit = new JMenuItem(messages.getExitMenu());
        menuItemExit.addActionListener(new ExitActionListener());
        cipherMenu.add(menuItemExit);
    }

    protected void buildDocumentMenu(JMenuBar menuBar) {
        JMenu documentMenu = new JMenu(messages.getDocumentMenu());
        menuBar.add(documentMenu);

        // Add Document
        JMenuItem menuItemAddDocument = new JMenuItem(messages.getAddDocumentMenu());
        menuItemAddDocument.addActionListener(new AddDocumentDelegate(documentController, messages));
        documentMenu.add(menuItemAddDocument);

        // Rename Document
        JMenuItem menuItemRenameDocument = new JMenuItem(messages.getRenameDocumentMenu());
        menuItemRenameDocument
                .addActionListener(new RenameDocumentDelegate(documentController, messages, parent.getDatamodel()));
        documentMenu.add(menuItemRenameDocument);

        // Save All
        JMenuItem menuItemSaveAll = new JMenuItem(messages.getSaveAllMenu());
        menuItemSaveAll.addActionListener(new SaveAllActionListener(documentController, parent.getDatamodel()));
        documentMenu.add(menuItemSaveAll);

        // Delete Document
        JMenuItem menuItemDeleteDocument = new JMenuItem(messages.getDeleteDocumentMenu());
        menuItemDeleteDocument.addActionListener(
                new DeleteDocumentActionListener(documentController, messages, parent, parent.getDatamodel()));
        documentMenu.addSeparator();
        documentMenu.add(menuItemDeleteDocument);
    }
}

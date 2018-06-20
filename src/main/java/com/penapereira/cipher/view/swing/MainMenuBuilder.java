package com.penapereira.cipher.view.swing;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentController;
import com.penapereira.cipher.view.swing.listener.AboutActionListener;
import com.penapereira.cipher.view.swing.listener.AddDocumentActionListener;
import com.penapereira.cipher.view.swing.listener.DeleteDocumentActionListener;
import com.penapereira.cipher.view.swing.listener.ExitActionListener;
import com.penapereira.cipher.view.swing.listener.SaveAllActionListener;

@Component
public class MainMenuBuilder {

    private Messages messages;
    private DocumentController documentController;
    private MainUserInterfaceImpl parent;

    @Autowired
    public MainMenuBuilder(DocumentController documentController, Messages messages) {
        this.messages = messages;
        this.documentController = documentController;
    }

    public void setMainUserInterface(MainUserInterfaceImpl parent) {
        this.parent = parent;
    }

    public JMenuBar buildJMenuBar() {
        JMenuBar menuBar = new JMenuBar();
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
        menuItemSaveAll.addActionListener(new SaveAllActionListener(documentController, parent));
        documentMenu.add(menuItemSaveAll);
        cipherMenu.addSeparator();

        JMenuItem menuItemDeleteDocument = new JMenuItem(messages.getDeleteDocumentMenu());
        menuItemDeleteDocument
                .addActionListener(new DeleteDocumentActionListener(documentController, messages, parent));
        documentMenu.addSeparator();
        documentMenu.add(menuItemDeleteDocument);

        return menuBar;
    }

}

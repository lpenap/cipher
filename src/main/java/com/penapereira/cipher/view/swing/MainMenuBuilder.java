package com.penapereira.cipher.view.swing;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
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

    private final Messages messages;
    private final DocumentController documentController;
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
        cipherMenu.setMnemonic(KeyEvent.VK_C);
        menuBar.add(cipherMenu);

        JMenuItem menuItemAbout = new JMenuItem(messages.getAboutMenu());
        menuItemAbout.setMnemonic(KeyEvent.VK_A);
        menuItemAbout.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        menuItemAbout.addActionListener(new AboutActionListener(messages));
        cipherMenu.add(menuItemAbout);
        cipherMenu.addSeparator();

        JMenuItem menuItemExit = new JMenuItem(messages.getExitMenu());
        menuItemExit.setMnemonic(KeyEvent.VK_X);
        menuItemExit.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menuItemExit.addActionListener(new ExitActionListener());
        cipherMenu.add(menuItemExit);
    }

    protected void buildDocumentMenu(JMenuBar menuBar) {
        JMenu documentMenu = new JMenu(messages.getDocumentMenu());
        documentMenu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(documentMenu);

        // Add Document
        JMenuItem menuItemAddDocument = new JMenuItem(messages.getAddDocumentMenu());
        menuItemAddDocument.setMnemonic(KeyEvent.VK_A);
        menuItemAddDocument.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_N,
                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menuItemAddDocument.addActionListener(new AddDocumentDelegate(documentController, messages));
        documentMenu.add(menuItemAddDocument);

        // Rename Document
        JMenuItem menuItemRenameDocument = new JMenuItem(messages.getRenameDocumentMenu());
        menuItemRenameDocument.setMnemonic(KeyEvent.VK_R);
        menuItemRenameDocument.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_R,
                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menuItemRenameDocument
                .addActionListener(new RenameDocumentDelegate(documentController, messages, parent.getDataModel()));
        documentMenu.add(menuItemRenameDocument);

        // Save All
        JMenuItem menuItemSaveAll = new JMenuItem(messages.getSaveAllMenu());
        menuItemSaveAll.setMnemonic(KeyEvent.VK_S);
        menuItemSaveAll.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S,
                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menuItemSaveAll.addActionListener(new SaveAllActionListener(documentController, parent.getDataModel()));
        documentMenu.add(menuItemSaveAll);

        // Delete Document
        JMenuItem menuItemDeleteDocument = new JMenuItem(messages.getDeleteDocumentMenu());
        menuItemDeleteDocument.setMnemonic(KeyEvent.VK_D);
        menuItemDeleteDocument.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        menuItemDeleteDocument.addActionListener(
                new DeleteDocumentActionListener(documentController, messages, parent, parent.getDataModel()));
        documentMenu.addSeparator();
        documentMenu.add(menuItemDeleteDocument);
    }
}

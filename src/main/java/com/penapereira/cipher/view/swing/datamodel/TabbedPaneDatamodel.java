package com.penapereira.cipher.view.swing.datamodel;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.view.swing.listener.CipherDocumentListener;

public class TabbedPaneDatamodel extends AbstractDatamodel<JTabbedPane, JScrollPane, JTextPane> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    protected JTabbedPane tabbedPaneDatamodel;

    @Override
    protected JTabbedPane isntanceWrappedDatamodel() {
        tabbedPaneDatamodel = new JTabbedPane(JTabbedPane.TOP);
        return tabbedPaneDatamodel;
    }

    @Override
    public synchronized void clearDatamodel() {
        tabbedPaneDatamodel.removeAll();
    }

    @Override
    public JTextPane buildDocumentContainer(Document doc) {
        JTextPane textPane = new JTextPane();
        StyledDocument styledDoc = textPane.getStyledDocument();
        CipherDocumentListener documentListener = new CipherDocumentListener();
        documentListener.setTabbedPane(tabbedPaneDatamodel);
        textPane.setFont(documentContainerFont);
        try {
            styledDoc.insertString(0, doc.getText(), styledDoc.getStyle("regular"));
        } catch (BadLocationException e) {
            log.error("Could not insert text from document '" + doc.getTitle()
                    + "' into the user interface, skipping...");
        }
        styledDoc.addDocumentListener(documentListener);
        return textPane;
    }

    @Override
    public JScrollPane buildDecorator(JTextPane documentContainer) {
        return new JScrollPane(documentContainer);
    }

    @Override
    public synchronized void addToDatamodel(Document doc, JScrollPane decorator) {
        tabbedPaneDatamodel.add(doc.getTitle(), decorator);
    }

    @Override
    public synchronized String getTextFromDocumentContainer(JTextPane documentContainer) {
        return documentContainer.getText();
    }

    @Override
    public synchronized void setDocumentContainerText(JTextPane documentContainer, String text) {
        documentContainer.setText(text);
    }

}

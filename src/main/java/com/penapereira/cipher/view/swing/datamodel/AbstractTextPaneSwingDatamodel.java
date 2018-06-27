package com.penapereira.cipher.view.swing.datamodel;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.view.swing.listener.CipherDocumentListener;

public abstract class AbstractTextPaneSwingDatamodel extends AbstractSwingDatamodel {

    private final Logger log = LoggerFactory.getLogger(AbstractTextPaneSwingDatamodel.class);

    @Override
    protected Pair<JComponent, JComponent> buildDocumentContainerHierarchy(Document doc) {
        JTextPane textPane = new JTextPane();
        StyledDocument styledDoc = textPane.getStyledDocument();
        CipherDocumentListener documentListener = new CipherDocumentListener(this, doc.getId());
        textPane.setFont(getDocumentContainerFont());
        try {
            styledDoc.insertString(0, doc.getText(), styledDoc.getStyle("regular"));
        } catch (BadLocationException e) {
            log.error("Could not insert text from document '" + doc.getTitle()
                    + "' into the user interface, skipping...");
        }
        styledDoc.addDocumentListener(documentListener);
        JScrollPane scrollPane = new JScrollPane(textPane);
        return Pair.of(scrollPane, textPane);
    }

    @Override
    protected String getTextFromChildComponent(JComponent component) {
        return ((JTextPane) component).getText();
    }
}

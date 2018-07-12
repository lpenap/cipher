package com.penapereira.cipher.view.swing.datamodel;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import com.penapereira.cipher.conf.Configuration;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.shared.RXTextUtilities;
import com.penapereira.cipher.shared.SwingUtil;
import com.penapereira.cipher.view.swing.component.JTextPaneSizeFilter;
import com.penapereira.cipher.view.swing.listener.CipherDocumentListener;

public abstract class AbstractTextPaneSwingDatamodel extends AbstractSwingDatamodel {

    @Autowired
    protected AbstractTextPaneSwingDatamodel(Configuration config) {
        super(config);
    }

    private final Logger log = LoggerFactory.getLogger(AbstractTextPaneSwingDatamodel.class);
    private SwingUtil util;

    @Override
    protected Pair<JComponent, JComponent> buildDocumentContainerHierarchy(Document doc) {
        this.util = new SwingUtil();
        JTextPane textPane = new JTextPane();
        StyledDocument styledDoc = textPane.getStyledDocument();
        CipherDocumentListener documentListener = new CipherDocumentListener(this, doc.getId());
        textPane.setFont(getDocumentContainerFont());
        try {
            if (styledDoc instanceof AbstractDocument) {
                ((AbstractDocument) styledDoc).setDocumentFilter(new JTextPaneSizeFilter(getMaxDocumentSizeChars()));
            }
            styledDoc.insertString(0, doc.getText(), new SwingUtil().getDefaultAttributeSet());
        } catch (BadLocationException e) {
            log.error("Could not insert text from document '" + doc.getTitle()
                    + "' into the user interface, skipping...");
        }
        styledDoc.addDocumentListener(documentListener);
        textPane.addFocusListener(documentListener);
        JScrollPane scrollPane = new JScrollPane(textPane);
        return Pair.of(scrollPane, textPane);
    }

    @Override
    protected synchronized String getTextFromChildComponent(JComponent component) {
        return ((JTextPane) component).getText();
    }

    @Override
    protected synchronized void setDocumentContainerText(JComponent component, String text) {
        ((JTextPane) component).setText(text);
    }

    @Override
    public synchronized void markText(Pair<Integer, Integer> indexes) {
        markTextWithAttributes(indexes, util.getAltAttributeSet());
    }

    @Override
    public synchronized void clearMarkedText(Pair<Integer, Integer> indexes) {
        markTextWithAttributes(indexes, util.getDefaultAttributeSet());
    }

    protected synchronized void markTextWithAttributes(Pair<Integer, Integer> indexes, AttributeSet atts) {
        JTextPane textPane = (JTextPane) getSelectedChildComponent();
        synchronized (textPane.getTreeLock()) {
            StyledDocument styledDoc = textPane.getStyledDocument();
            styledDoc.setCharacterAttributes(indexes.getFirst(), indexes.getSecond() - indexes.getFirst(), atts, false);
            textPane.setCaretPosition(indexes.getFirst());
            RXTextUtilities.centerLineInScrollPane(textPane);
        }
    }

    @Override
    public synchronized void resetTextAttributesOfSelectedComponent() {
        log.trace("Reseting document style to default");
        JTextPane textPane = (JTextPane) getSelectedChildComponent();
        synchronized (textPane.getTreeLock()) {
            StyledDocument styledDoc = textPane.getStyledDocument();
            styledDoc.setCharacterAttributes(0, styledDoc.getLength(), util.getDefaultAttributeSet(), true);
        }
    }
}

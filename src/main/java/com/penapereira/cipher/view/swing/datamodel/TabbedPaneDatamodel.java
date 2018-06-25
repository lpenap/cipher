package com.penapereira.cipher.view.swing.datamodel;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.view.swing.listener.CipherDocumentListener;

public class TabbedPaneDatamodel extends AbstractDatamodel<JTabbedPane, JScrollPane, JTextPane> {

    protected final String MODIFIED_PREFIX = "* \u2063";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected JTabbedPane isntanceWrappedDatamodel() {
        return new JTabbedPane(JTabbedPane.TOP);
    }

    @Override
    public synchronized void clearDatamodel() {
        getDatamodel().removeAll();
    }

    @Override
    public JTextPane buildDocumentContainer(Document doc) {
        JTextPane textPane = new JTextPane();
        StyledDocument styledDoc = textPane.getStyledDocument();
        CipherDocumentListener documentListener = new CipherDocumentListener(textPane);
        documentListener.setDatamodel(this);
        textPane.setFont(getDocumentContainerFont());
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
    public synchronized Integer addToDatamodel(JScrollPane decorator, String name) {
        decorator.setName(name);
        getDatamodel().add(name, decorator);
        return getScrollPaneIndex(decorator);
    }

    @Override
    public synchronized String getTextFromDocumentContainer(JTextPane documentContainer) {
        return documentContainer.getText();
    }

    @Override
    public synchronized void setDocumentContainerText(JTextPane documentContainer, String text) {
        documentContainer.setText(text);
    }

    @Override
    protected void setDecoratorName(JScrollPane scrollPane, String name) {
        int index = getScrollPaneIndex(scrollPane);
        if (index != -1) {
            log.trace("Updating tab name at index {} with '{}'", index, name);
            getDatamodel().setTitleAt(index, name);
        }
    }

    protected int getScrollPaneIndex(JScrollPane scrollPane) {
        log.trace("Searching tab index of '{}'", scrollPane.getName());
        for (int i = 0; i < getDatamodel().getTabCount(); i++) {
            JScrollPane currentScrollPane = (JScrollPane) getDatamodel().getComponentAt(i);
            if (scrollPane == currentScrollPane) {
                log.trace("Tab found at index {}", i);
                return i;
            }
        }
        return -1;
    }

    @Override
    public void setModifiedNameFor(JTextPane textPane) {
        JScrollPane scrollPane = findDecoratorForDocumentContainer(textPane);
        int scrollPaneIndex = getScrollPaneIndex(scrollPane);
        if (!isDecoratorMarkedAsModified(scrollPane)) {
            String tabName = MODIFIED_PREFIX + scrollPane.getName();
            log.trace("Updating tab name to '{}'", tabName);
            getDatamodel().setTitleAt(scrollPaneIndex, tabName);
            markDecoratorAsModified(scrollPane);
        }
    }

    @Override
    protected void requestFocus(int decoratorIndex) {
        getDatamodel().setSelectedIndex(decoratorIndex);
    }

    @Override
    protected void removeFromWrappedDatamodel(JScrollPane scrollPane) {
        getDatamodel().remove(scrollPane);
    }

    @Override
    public void addWrapedDatamodelChangeListener(ChangeListener listener) {
        getDatamodel().addChangeListener(listener);
    }
}

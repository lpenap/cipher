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

    protected final String MODIFIED_PREFIX = "* \u2063";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected JTabbedPane isntanceWrappedDatamodel() {
        datamodel = new JTabbedPane(JTabbedPane.TOP);
        return datamodel;
    }

    @Override
    public synchronized void clearDatamodel() {
        datamodel.removeAll();
    }

    @Override
    public JTextPane buildDocumentContainer(Document doc) {
        JTextPane textPane = new JTextPane();
        StyledDocument styledDoc = textPane.getStyledDocument();
        CipherDocumentListener documentListener = new CipherDocumentListener();
        documentListener.setDatamodel(this);
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
    public synchronized Integer addToDatamodel(JScrollPane decorator, String name) {
        Integer lastIndex = datamodel.getTabCount();
        decorator.setName(name);
        datamodel.add(decorator, lastIndex);
        return lastIndex;
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
    protected void setDecoratorNameAt(Integer index, String name) {
        log.trace("Updating tab name at {} with {}", index, name);
        datamodel.setTitleAt(index, name);
    }

    @Override
    public void setModifiedNameOfSelectedComponent() {
        int index = datamodel.getSelectedIndex();
        if (!decoratorIndexToIsModifiedMap.get(index)) {
            JScrollPane selectedComponent = (JScrollPane) datamodel.getSelectedComponent();
            String tabName = selectedComponent.getName();
            setDecoratorNameAt(index, MODIFIED_PREFIX + tabName);
        }
        decoratorIndexToIsModifiedMap.put(index, true);
    }
}

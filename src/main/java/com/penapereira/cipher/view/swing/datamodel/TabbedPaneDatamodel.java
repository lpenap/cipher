package com.penapereira.cipher.view.swing.datamodel;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.view.swing.listener.CipherDocumentListener;
import com.penapereira.cipher.view.swing.search.SearchAdapter;

@Component
public class TabbedPaneDatamodel extends AbstractSwingDatamodel {

    protected final String MODIFIED_PREFIX = "* \u2063";

    private final Logger log = LoggerFactory.getLogger(getClass());

    protected JComponent buildMainComponent() {
        return new JTabbedPane(JTabbedPane.TOP);
    }

    @Override
    public int getComponentCount() {
        return ((JTabbedPane) getMainComponent()).getTabCount();
    }

    @Override
    public JComponent getComponentAt(int index) {
        return (JComponent) ((JTabbedPane) getMainComponent()).getComponentAt(index);
    }

    @Override
    public JComponent getSelectedComponent() {
        return (JComponent) ((JTabbedPane) getMainComponent()).getSelectedComponent();
    }

    @Override
    public synchronized void clearDatamodel() {
        ((JTabbedPane) getMainComponent()).removeAll();
    }

    @Override
    protected void requestFocus(int decoratorIndex) {
        ((JTabbedPane) getMainComponent()).setSelectedIndex(decoratorIndex);
    }

    @Override
    public synchronized Integer addToMainComponent(JComponent parent, Document doc) {
        parent.setName(doc.getTitle());
        ((JTabbedPane) getMainComponent()).add(doc.getTitle(), parent);

        return getComponentIndex(parent);
    }

    protected int getComponentIndex(JComponent component) {
        log.trace("Searching tab index of '{}'", component.getName());
        for (int i = 0; i < getComponentCount(); i++) {
            JComponent currentComponent = (JComponent) ((JTabbedPane) getMainComponent()).getComponentAt(i);
            if (component == currentComponent) {
                log.trace("Tab found at index {}", i);
                return i;
            }
        }
        return -1;
    }

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
    public void setModifiedNameFor(Long documentId) {
        JComponent parentComponent = findParentOf(documentId);
        int parentComponentIndex = getComponentIndex(parentComponent);
        if (!isParentComponentModified(parentComponent)) {
            String tabName = MODIFIED_PREFIX + parentComponent.getName();
            log.trace("Updating tab name to '{}'", tabName);
            ((JTabbedPane) getMainComponent()).setTitleAt(parentComponentIndex, tabName);
            markParentComponentAsModified(parentComponent);
        }
    }

    @Override
    protected String getTextFromChildComponent(JComponent component) {
        return ((JTextPane) component).getText();
    }

    @Override
    protected void setDocumentContainerText(JComponent component, String text) {
        ((JTextPane) component).setText(text);
    }

    @Override
    protected void setParentComponentName(JComponent component, String name) {
        int index = getComponentIndex(component);
        if (index != -1) {
            log.trace("Updating tab name at index {} with '{}'", index, name);
            ((JTabbedPane) getMainComponent()).setTitleAt(index, name);
        }
    }

    @Override
    protected void removeParentComponent(JComponent component) {
        ((JTabbedPane) getMainComponent()).remove(component);
    }

    @Override
    public void addSearchAdapter(SearchAdapter searchAdapter) {
        ((JTabbedPane) getMainComponent()).addChangeListener(searchAdapter);
    }
}

package com.penapereira.cipher.view.swing.datamodel;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import com.penapereira.cipher.conf.Configuration;
import com.penapereira.cipher.model.document.Document;
import lombok.Data;

@Data
public abstract class AbstractSwingDatamodel implements SwingDatamodelInterface {

    private final int MAX_DOCUMENT_SIZE_CHARS_DEFAULT = 100000000;

    private final Logger log = LoggerFactory.getLogger(AbstractSwingDatamodel.class);
    protected List<Document> documents;
    protected Map<JComponent, JComponent> parentToChildComponentMap;
    protected Map<JComponent, Long> parentToDocumentIdMap;
    protected Map<Long, JComponent> documentIdToParentMap;
    protected Map<JComponent, Boolean> isParentComponentModifiedMap;
    protected JComponent mainComponent;
    protected String documentFont;
    protected int documentFontSize;
    protected Font documentContainerFont;
    protected Configuration config;

    @Autowired
    protected AbstractSwingDatamodel(Configuration config) {
        this.config = config;
        documents = new ArrayList<>();
        initArrays();
        documentFont = config.getDocumentFont();
        documentFontSize = config.getDocumentFontSize();
        mainComponent = buildMainComponent();

    }

    protected abstract JComponent buildMainComponent();

    protected abstract Pair<JComponent, JComponent> buildDocumentContainerHierarchy(Document doc);

    protected abstract Integer addToMainComponent(JComponent parent, Document doc);

    protected abstract void requestFocus(int decoratorIndex);

    protected abstract String getTextFromChildComponent(JComponent component);

    protected abstract void setDocumentContainerText(JComponent component, String text);

    protected abstract void setParentComponentName(JComponent component, String name);

    protected abstract void removeParentComponent(JComponent component);

    protected int getMaxDocumentSizeChars() {
        return Math.min(MAX_DOCUMENT_SIZE_CHARS_DEFAULT, config.getMaxDocumentSizeChars());
    }

    public synchronized void setDocuments(List<Document> documents) {
        this.documents = documents;
        build();
    }

    protected void initArrays() {
        parentToChildComponentMap = new HashMap<>();
        parentToDocumentIdMap = new HashMap<>();
        documentIdToParentMap = new HashMap<>();
        isParentComponentModifiedMap = new HashMap<>();
    }

    protected void build() {
        clearDatamodel();
        initArrays();
        Iterator<Document> i = documents.iterator();
        while (i.hasNext()) {
            Document doc = i.next();
            addDocumentDatamodel(doc);
        }
    }

    protected void addDocumentDatamodel(Document doc) {
        Pair<JComponent, JComponent> parentChild = buildDocumentContainerHierarchy(doc);
        JComponent parent = parentChild.getFirst();
        JComponent child = parentChild.getSecond();
        parentToChildComponentMap.put(parent, child);
        documentIdToParentMap.put(doc.getId(), parent);
        parentToDocumentIdMap.put(parent, doc.getId());
        isParentComponentModifiedMap.put(parent, false);

        Integer index = addToMainComponent(parent, doc);
        requestFocus(index);
    }

    protected JComponent findParentOf(JComponent child) {
        JComponent decoratorFound = null;
        Iterator<Entry<JComponent, JComponent>> i = parentToChildComponentMap.entrySet().iterator();
        while (i.hasNext()) {
            Entry<JComponent, JComponent> entry = i.next();
            if (entry.getValue() == child) {
                decoratorFound = entry.getKey();
                break;
            }
        }
        return decoratorFound;
    }

    protected JComponent findParentOf(Long documentId) {
        return documentIdToParentMap.get(documentId);
    }

    protected boolean isParentComponentModified(JComponent component) {
        return isParentComponentModifiedMap.get(component);
    }

    protected void markParentComponentAsModified(JComponent component) {
        log.trace("marking decorator as modified. decoratorToIsModifiedMap size: {}",
                isParentComponentModifiedMap.size());
        isParentComponentModifiedMap.put(component, true);
    }

    @Override
    public void updateDocument(Document doc) {
        JComponent parent = documentIdToParentMap.get(doc.getId());
        JComponent child = parentToChildComponentMap.get(parent);
        isParentComponentModifiedMap.put(parent, true);
        setDocumentContainerText(child, doc.getText());
        setParentComponentName(parent, doc.getTitle());
        isParentComponentModifiedMap.put(parent, false);

    }

    @Override
    public void deleteDocument(Document doc) {
        Document oldDocument = findById(doc.getId());
        if (oldDocument != null) {
            Long docId = doc.getId();
            JComponent parent = documentIdToParentMap.get(docId);
            documentIdToParentMap.remove(docId);
            parentToDocumentIdMap.remove(parent);
            parentToChildComponentMap.remove(parent);
            isParentComponentModifiedMap.remove(parent);
            removeParentComponent(parent);
            documents.remove(oldDocument);
        }
    }

    private Document findById(Long id) {
        Iterator<Document> i = documents.iterator();
        Document foundDocument = null;
        while (i.hasNext()) {
            Document doc = i.next();
            if (doc.getId() == id) {
                foundDocument = doc;
                break;
            }
        }
        return foundDocument;
    }

    @Override
    public String getTextFromComponent(JComponent component) {
        return getTextFromChildComponent(parentToChildComponentMap.get(component));
    }

    public synchronized void addDocument(Document doc) {
        documents.add(doc);
        addDocumentDatamodel(doc);
    }

    @Override
    public Long getDocumentIdFor(JComponent component) {
        return parentToDocumentIdMap.get(component);
    }

    @Override
    public JComponent getSelectedChildComponent() {
        return parentToChildComponentMap.get(getSelectedComponent());
    }
}

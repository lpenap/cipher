package com.penapereira.cipher.view.swing.datamodel;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.model.document.Document;
import lombok.Data;

@Data
public abstract class AbstractDatamodel<M, D, C> implements DatamodelInterface<M, D, C> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    protected List<Document> documents;
    protected Map<D, Long> decoratorToIdMap;
    protected Map<Long, D> idToDecoratorMap;
    protected Map<D, C> decoratorToDocumentContainerMap;
    protected Map<D, Boolean> decoratorToIsModifiedMap;

    protected String documentFont;
    protected int documentFontSize;
    protected M datamodel;

    protected Font documentContainerFont;

    protected AbstractDatamodel() {
        documents = new ArrayList<Document>();
        initArrays();
        documentFont = "Courier";
        documentFontSize = 12;
        datamodel = isntanceWrappedDatamodel();
    }

    protected abstract M isntanceWrappedDatamodel();

    protected abstract C buildDocumentContainer(Document doc);

    protected abstract D buildDecorator(C documentContainer);

    protected abstract void setDocumentContainerText(C documentContainer, String text);

    protected abstract String getTextFromDocumentContainer(C documentContainer);

    protected abstract Integer addToDatamodel(D decorator, String name);

    protected abstract void setDecoratorName(D decorator, String name);

    protected abstract void requestFocus(int decoratorIndex);

    protected abstract void removeFromWrappedDatamodel(D decorator);

    protected void initArrays() {
        decoratorToIdMap = new HashMap<D, Long>();
        decoratorToDocumentContainerMap = new HashMap<D, C>();
        idToDecoratorMap = new HashMap<Long, D>();
        decoratorToIsModifiedMap = new HashMap<D, Boolean>();
    }

    public synchronized void setDocuments(List<Document> documents) {
        this.documents = documents;
        build();
    }

    public synchronized M getWrappedDatamodel() {
        if (datamodel == null) {
            build();
        }
        return datamodel;
    }

    public synchronized void setDocumentContainerFont(Font font) {
        this.documentContainerFont = font;
    }

    public synchronized Font getDocumentContainerFont() {
        return this.documentContainerFont;
    }

    public synchronized Long getDocumentIdFor(D decorator) {
        return decoratorToIdMap.get(decorator);
    }

    public synchronized String getTextFromDecorator(D decorator) {
        return getTextFromDocumentContainer(decoratorToDocumentContainerMap.get(decorator));
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

    public synchronized void updateDatamodelForDocument(Document doc) {
        D decorator = idToDecoratorMap.get(doc.getId());
        C documentContainer = decoratorToDocumentContainerMap.get(decorator);
        decoratorToIsModifiedMap.put(decorator, true);
        setDocumentContainerText(documentContainer, doc.getText());
        setDecoratorName(decorator, doc.getTitle());
        decoratorToIsModifiedMap.put(decorator, false);
    }

    public synchronized void addDocument(Document doc) {
        documents.add(doc);
        addDocumentDatamodel(doc);
    }

    protected void addDocumentDatamodel(Document doc) {
        C documentContainer = buildDocumentContainer(doc);
        D decorator = buildDecorator(documentContainer);

        idToDecoratorMap.put(doc.getId(), decorator);
        decoratorToIdMap.put(decorator, doc.getId());
        decoratorToDocumentContainerMap.put(decorator, documentContainer);

        Integer index = addToDatamodel(decorator, doc.getTitle());
        decoratorToIsModifiedMap.put(decorator, false);

        requestFocus(index);
    }

    public synchronized void deleteDocument(Document doc) {
        if (documents.contains(doc)) {
            Long docId = doc.getId();
            D decorator = idToDecoratorMap.get(docId);
            idToDecoratorMap.remove(docId);
            decoratorToIdMap.remove(decorator);
            decoratorToDocumentContainerMap.remove(decorator);
            decoratorToIsModifiedMap.remove(decorator);
            removeFromWrappedDatamodel(decorator);
        }
        documents.remove(doc);
    }

    protected boolean isDecoratorMarkedAsModified(D decorator) {
        return decoratorToIsModifiedMap.get(decorator);
    }

    protected void markDecoratorAsModified(D decorator) {
        log.trace("marking decorator as modified. decoratorToIsModifiedMap size: {}", decoratorToIsModifiedMap.size());
        decoratorToIsModifiedMap.put(decorator, true);
    }

    protected D findDecoratorForDocumentContainer(C documentContainer) {
        D decoratorFound = null;
        Iterator<Entry<D, C>> i = decoratorToDocumentContainerMap.entrySet().iterator();
        while (i.hasNext()) {
            Entry<D, C> entry = i.next();
            if (entry.getValue() == documentContainer) {
                decoratorFound = entry.getKey();
                break;
            }
        }
        return decoratorFound;
    }
}

package com.penapereira.cipher.view.swing.datamodel;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.penapereira.cipher.model.document.Document;
import lombok.Data;

@Data
public abstract class AbstractDatamodel<M, D, C> implements DatamodelInterface<M, D, C> {

    protected List<Document> documents;
    protected Map<D, Long> decoratorToIdMap;
    protected Map<Long, D> idToDecoratorMap;
    protected Map<D, C> decoratorToDocumentContainerMap;
    protected Map<D, Integer> decoratorIndex;
    protected Map<Integer, Boolean> decoratorIndexToIsModifiedMap;

    protected String documentFont;
    protected int documentFontSize;
    protected M datamodel;

    protected Font documentContainerFont;

    protected AbstractDatamodel() {
        documents = new ArrayList<Document>();
        decoratorToIdMap = new HashMap<D, Long>();
        decoratorToDocumentContainerMap = new HashMap<D, C>();
        idToDecoratorMap = new HashMap<Long, D>();
        decoratorIndex = new HashMap<D, Integer>();
        decoratorIndexToIsModifiedMap = new HashMap<Integer, Boolean>();
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

    protected abstract void setDecoratorNameAt(Integer index, String name);

    protected abstract void requestFocus(int decoratorIndex);

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
        Iterator<Document> i = documents.iterator();
        while (i.hasNext()) {
            Document doc = i.next();
            addDocument(doc);
        }
    }

    public synchronized void updateDatamodelForDocument(Document doc) {
        D decorator = idToDecoratorMap.get(doc.getId());
        C documentContainer = decoratorToDocumentContainerMap.get(decorator);
        setDocumentContainerText(documentContainer, doc.getText());
        setDecoratorNameAt(decoratorIndex.get(decorator), doc.getTitle());
        decoratorIndexToIsModifiedMap.put(decoratorIndex.get(decorator), false);
    }

    public synchronized void addDocument(Document doc) {
        C documentContainer = buildDocumentContainer(doc);
        D decorator = buildDecorator(documentContainer);

        idToDecoratorMap.put(doc.getId(), decorator);
        decoratorToIdMap.put(decorator, doc.getId());
        decoratorToDocumentContainerMap.put(decorator, documentContainer);

        Integer index = addToDatamodel(decorator, doc.getTitle());
        decoratorIndexToIsModifiedMap.put(index, false);
        decoratorIndex.put(decorator, index);

        requestFocus(index);
    }

    protected boolean isDecoratorMarkedAsModified(int index) {
        return decoratorIndexToIsModifiedMap.get(index);
    }

    protected void markDecoratorAsModified(int index) {
        decoratorIndexToIsModifiedMap.put(index, true);
    }
}

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

    protected String documentFont;
    protected int documentFontSize;
    protected M datamodel;

    protected Font documentContainerFont;

    protected AbstractDatamodel() {
        documents = new ArrayList<Document>();
        decoratorToIdMap = new HashMap<D, Long>();
        decoratorToDocumentContainerMap = new HashMap<D, C>();
        idToDecoratorMap = new HashMap<Long, D>();
        documentFont = "Courier";
        documentFontSize = 12;
        datamodel = isntanceWrappedDatamodel();
    }

    protected abstract M isntanceWrappedDatamodel();

    protected abstract C buildDocumentContainer(Document doc);

    protected abstract D buildDecorator(C documentContainer);

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

    public synchronized Long getDocumentIdFor(D decorator) {
        return decoratorToIdMap.get(decorator);
    }

    public synchronized String getTextFromDecorator(D decorator) {
        return getTextFromDocumentContainer(decoratorToDocumentContainerMap.get(decorator));
    }

    protected abstract String getTextFromDocumentContainer(C documentContainer);

    public synchronized void updateDatamodelForDocument(Document doc) {
        D decorator = idToDecoratorMap.get(doc.getId());
        C documentContainer = decoratorToDocumentContainerMap.get(decorator);

        setDocumentContainerText(documentContainer, doc.getText());
    }

    protected void build() {
        clearDatamodel();
        Iterator<Document> i = documents.iterator();
        while (i.hasNext()) {
            Document doc = i.next();
            C documentContainer = buildDocumentContainer(doc);
            D decorator = buildDecorator(documentContainer);

            idToDecoratorMap.put(doc.getId(), decorator);
            decoratorToIdMap.put(decorator, doc.getId());
            decoratorToDocumentContainerMap.put(decorator, documentContainer);

            addToDatamodel(doc, decorator);
        }
    }
}

package com.penapereira.cipher.view.swing.datamodel;

import java.awt.Font;
import java.util.List;
import com.penapereira.cipher.model.document.Document;

public interface DatamodelFactoryInterface<M, D, C> {

    M isntanceDatamodel();

    void clearDatamodel();

    M getDatamodel();

    C buildDocumentContainer(Document doc);

    D buildDecorator(C documentContainer);

    void addToDatamodel(Document doc, D decorator);

    Long getDocumentIdFor(D decorator);

    String getTextFromDecorator(D decorator);

    String getTextFromDocumentContainer(C documentContainer);

    void setDocumentContainerText(C documentContainer, String text);

    void updateDatamodelForDocument(Document doc);

    void setDocumentContainerFont(Font font);

    void setDocuments(List<Document> documents);
}

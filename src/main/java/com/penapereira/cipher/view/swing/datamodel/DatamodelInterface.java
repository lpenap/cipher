package com.penapereira.cipher.view.swing.datamodel;

import java.awt.Font;
import java.util.List;
import com.penapereira.cipher.model.document.Document;

public interface DatamodelInterface<M, D, C> {

    void clearDatamodel();

    M getWrappedDatamodel();

    void addToDatamodel(Document doc, D decorator);

    Long getDocumentIdFor(D decorator);

    String getTextFromDecorator(D decorator);

    void setDocumentContainerText(C documentContainer, String text);

    void updateDatamodelForDocument(Document doc);

    void setDocumentContainerFont(Font font);

    void setDocuments(List<Document> documents);
}

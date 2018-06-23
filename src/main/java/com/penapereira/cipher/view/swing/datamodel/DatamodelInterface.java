package com.penapereira.cipher.view.swing.datamodel;

import java.awt.Font;
import java.util.List;
import com.penapereira.cipher.model.document.Document;

/**
 * Class to wrap a Swing interface data model for all the documents to be rendered. It lets define a 3 level data model:
 * A main component to hold all the component, A decorator component to decorate each element and a document container
 * component to render text.
 * 
 * @author luis
 *
 * @param <M> Wrapped class to represent the main component to be rendered (i.e. JTabbedPane).
 * @param <D> Additional Swing decorator component to be added in the main component for each document. (i.e.
 *        JScrollPane)
 * @param <C> Document container component to hold the text value of each document (i.e. JTextPane)
 */
public interface DatamodelInterface<M, D, C> {

    void clearDatamodel();

    M getWrappedDatamodel();

    Long getDocumentIdFor(D decorator);

    String getTextFromDecorator(D decorator);

    void updateDatamodelForDocument(Document doc);

    void setDocumentContainerFont(Font font);

    void setDocuments(List<Document> documents);
    
    void setModifiedNameOfSelectedComponent();
}

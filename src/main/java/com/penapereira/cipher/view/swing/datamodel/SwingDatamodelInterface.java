package com.penapereira.cipher.view.swing.datamodel;

import java.awt.Font;
import java.util.List;
import javax.swing.JComponent;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.view.swing.search.SearchAdapter;

/**
 * Class to wrap a Swing user interface data model for all the documents to be rendered.
 */
public interface SwingDatamodelInterface {

    void setDocumentContainerFont(Font font);

    void updateDocument(Document doc);

    void addDocument(Document doc);

    void deleteDocument(Document doc);

    JComponent getMainComponent();

    void setDocuments(List<Document> documents);

    int getComponentCount();

    JComponent getComponentAt(int index);

    String getTextFromComponent(JComponent component);

    Long getDocumentIdFor(JComponent component);

    JComponent getSelectedComponent();

    void clearDatamodel();

    void setModifiedNameFor(Long documentId);

    void addSearchAdapter(SearchAdapter searchAdapter);
}

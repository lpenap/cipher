package com.penapereira.cipher.view.swing.datamodel;

import java.awt.Font;
import java.util.List;
import javax.swing.JComponent;
import org.springframework.data.util.Pair;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.view.swing.search.SearchAdapter;

/**
 * Class to wrap a Swing user interface data model for all the documents to be rendered.
 */
public interface SwingDataModelInterface {

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

    void clearDataModel();

    void setModifiedNameFor(Long documentId);

    void addSearchAdapter(SearchAdapter searchAdapter);

    JComponent getSelectedChildComponent();

    void markText(Pair<Integer, Integer> indexes);

    void clearMarkedText(Pair<Integer, Integer> indexes);

    void resetTextAttributesOfSelectedComponent();
}

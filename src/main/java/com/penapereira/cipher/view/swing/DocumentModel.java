package com.penapereira.cipher.view.swing;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.view.swing.listener.CipherDocumentListener;
import lombok.Data;

@Data
public class DocumentModel {

    protected static DocumentModel singleInstance = null;
    protected static final Logger log = LoggerFactory.getLogger(DocumentModel.class);

    protected List<Document> documents;
    protected Map<JScrollPane, Long> scrollPaneToIdMap;
    protected Map<JScrollPane, JTextPane> scrollPaneToTextPaneMap;

    protected String documentFont;
    protected int documentFontSize;
    protected JTabbedPane tabbedPane;

    protected DocumentModel() {
        documents = new ArrayList<>();
        scrollPaneToIdMap = new HashMap<>();
        scrollPaneToTextPaneMap = new HashMap<>();
        documentFont = "Courier";
        documentFontSize = 12;
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    }

    public synchronized static DocumentModel instance() {
        if (singleInstance == null) {
            singleInstance = new DocumentModel();
        }
        return singleInstance;
    }

    public synchronized void setDocuments(List<Document> documents) {
        this.documents = documents;
        buildTabbedPane();
    }

    public synchronized JTabbedPane getTabbedPane() {
        if (tabbedPane.getTabCount() == 0) {
            buildTabbedPane();
        }
        return tabbedPane;
    }

    protected void buildTabbedPane() {
        tabbedPane.removeAll();
        Iterator<Document> i = documents.iterator();
        while (i.hasNext()) {
            Document doc = i.next();
            JTextPane textPane = new JTextPane();
            StyledDocument styledDoc = textPane.getStyledDocument();
            CipherDocumentListener documentListener = new CipherDocumentListener();
            styledDoc.addDocumentListener(documentListener);
            textPane.setFont(new Font(documentFont, Font.PLAIN, documentFontSize));
            try {
                styledDoc.insertString(styledDoc.getLength(), doc.getText(), styledDoc.getStyle("regular"));
            } catch (BadLocationException e) {
                log.error("Could not insert text from document '" + doc.getTitle()
                        + "' into the user interface, skipping...");
                continue;
            }
            JScrollPane scrollPane = new JScrollPane(textPane);
            scrollPaneToIdMap.put(scrollPane, doc.getId());
            scrollPaneToTextPaneMap.put(scrollPane, textPane);
            tabbedPane.addTab(doc.getTitle(), scrollPane);
            documentListener.setTabbedPane(tabbedPane);
        }
    }

    public synchronized Long getDocumentIdFor(JScrollPane selectedScrollPane) {
        return scrollPaneToIdMap.get(selectedScrollPane);
    }

    public synchronized String getTextFrom(JScrollPane scrollPane) {
        return scrollPaneToTextPaneMap.get(scrollPane).getText();
    }
}

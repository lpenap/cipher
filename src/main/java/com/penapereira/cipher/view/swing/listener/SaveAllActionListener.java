package com.penapereira.cipher.view.swing.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.controller.DocumentController;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.view.swing.MainUserInterfaceImpl;

public class SaveAllActionListener extends AbstractActionListener implements ActionListener {

    private final static Logger log = LoggerFactory.getLogger(SaveAllActionListener.class);
    private static final long serialVersionUID = 1L;
    private MainUserInterfaceImpl parent;

    public SaveAllActionListener(DocumentController documentController, MainUserInterfaceImpl parent) {
        super(documentController);
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        log.debug("Saving all documents...");
        List<Document> documents = new ArrayList<>();
        for (int i = 0; i < parent.getDocumentsTabbedPane().getTabCount(); i++) {
            JScrollPane scrollPane = (JScrollPane) parent.getDocumentsTabbedPane().getComponentAt(i);
            String text = parent.getTextFromScrollPane(scrollPane);
            Long documentId = parent.getDocumentIdFromScrollPane(scrollPane);
            Document doc = documentController.get(documentId);
            doc.setText(text);
            documents.add(doc);
        }
        documentController.saveAll(documents);
        log.debug("Saving done.");
    }

    @Override
    protected void build() {}

}

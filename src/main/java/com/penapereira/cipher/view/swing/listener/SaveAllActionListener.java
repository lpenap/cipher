package com.penapereira.cipher.view.swing.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.controller.DocumentController;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.view.swing.datamodel.DatamodelInterface;

public class SaveAllActionListener extends AbstractActionListener implements ActionListener {

    private final static Logger log = LoggerFactory.getLogger(SaveAllActionListener.class);
    private static final long serialVersionUID = 1L;
    private DatamodelInterface<JTabbedPane, JScrollPane, JTextPane> datamodel;

    public SaveAllActionListener(DocumentController documentController,
            DatamodelInterface<JTabbedPane, JScrollPane, JTextPane> datamodel) {
        super(documentController);
        this.datamodel = datamodel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        log.debug("Saving all documents...");
        List<Document> documents = new ArrayList<>();
        for (int i = 0; i < datamodel.getWrappedDatamodel().getTabCount(); i++) {
            JScrollPane scrollPane = (JScrollPane) datamodel.getWrappedDatamodel().getComponentAt(i);
            String text = datamodel.getTextFromDecorator(scrollPane);
            Long documentId = datamodel.getDocumentIdFor(scrollPane);
            Document doc = documentController.get(documentId);
            doc.setText(text);
            documents.add(doc);
        }
        documentController.saveAll(documents);
        log.debug("Saving all documents... Done");
    }

    @Override
    protected void build() {}

}

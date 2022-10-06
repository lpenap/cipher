package com.penapereira.cipher.view.swing.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.penapereira.cipher.controller.DocumentController;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.view.swing.datamodel.SwingDataModelInterface;

public class SaveAllActionListener extends AbstractActionListener implements ActionListener {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final long serialVersionUID = 1L;
    private final SwingDataModelInterface dataModel;

    public SaveAllActionListener(DocumentController documentController, SwingDataModelInterface dataModel) {
        super(documentController);
        this.dataModel = dataModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        log.debug("Saving all documents...");
        List<Document> documents = new ArrayList<>();
        for (int i = 0; i < dataModel.getComponentCount(); i++) {
            JComponent component = dataModel.getComponentAt(i);
            String text = dataModel.getTextFromComponent(component);
            Long documentId = dataModel.getDocumentIdFor(component);
            Document doc = documentController.get(documentId).get();
            doc.setText(text);
            documents.add(doc);
        }
        documentController.saveAll(documents);
        log.debug("Saving all documents... Done");
    }

    @Override
    protected void build() {}

}

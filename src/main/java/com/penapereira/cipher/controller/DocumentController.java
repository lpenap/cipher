package com.penapereira.cipher.controller;

import com.penapereira.cipher.conf.Configuration;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.model.document.DocumentService;
import com.penapereira.cipher.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Optional;

@Component
public class DocumentController implements DocumentChangeSupportInterface {
    protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    @Autowired
    ActionType actionType;

    @Autowired
    Configuration config;

    @Autowired
    DocumentService documentService;

    @Autowired
    Messages messages;

    public List<Document> getAll() {
        return documentService.findAll();
    }

    public Document getHelpDocument() {
        return documentService.create(messages.getHelpDocumentTitle(),
                new StringUtil().linesToPage(messages.getHelpDocument()));
    }

    public Document save(Document doc) {
        Document savedDoc = documentService.save(doc);
        pcs.firePropertyChange(actionType.UDPATE, null, savedDoc);
        return savedDoc;
    }

    public List<Document> saveAll(List<Document> documents) {
        List<Document> savedDocs = documentService.saveAll(documents);
        pcs.firePropertyChange(actionType.UPDATE_ALL ,null, savedDocs);
        return savedDocs;
    }

    public Document createAndSaveDocument(String title, String text) {
        Document newDoc = documentService.create(title, text);
        newDoc = documentService.save(newDoc);
        pcs.firePropertyChange(actionType.ADD, null, newDoc);
        return newDoc;
    }

    public Optional<Document> get(Long documentId) {
        return documentService.findById(documentId);
    }

    public void delete(Document doc) {
        if (doc != null) {
            documentService.delete(doc);
            pcs.firePropertyChange(actionType.DELETE, doc, null);
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void fireUpdateAll() {
        pcs.firePropertyChange(actionType.UPDATE_ALL, null, documentService.findAll());
    }
}

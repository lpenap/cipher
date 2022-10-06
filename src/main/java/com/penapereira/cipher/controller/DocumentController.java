package com.penapereira.cipher.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.penapereira.cipher.conf.Configuration;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentActionInterface.ActionType;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.model.document.DocumentService;
import com.penapereira.cipher.shared.StringUtil;

@Component
public class DocumentController extends Observable {

    protected final Configuration config;
    protected final DocumentService documentService;
    protected final Messages messages;

    @Autowired
    public DocumentController(Configuration config, DocumentService documentService, Messages messages) {
        super();
        this.config = config;
        this.documentService = documentService;
        this.messages = messages;
    }

    public List<Document> getAll() {
        return documentService.findAll();
    }

    public Document getHelpDocument() {
        return documentService.create(messages.getHelpDocumentTitle(),
                new StringUtil().linesToPage(messages.getHelpDocument()));
    }

    public Document save(Document doc) {
        Document savedDoc = documentService.save(doc);
        requestNotifyObservers(ActionType.UPDATE, savedDoc);
        return savedDoc;
    }

    public List<Document> saveAll(List<Document> documents) {
        List<Document> savedDocs = documentService.saveAll(documents);
        requestNotifyObservers(savedDocs);
        return savedDocs;
    }

    public Document createAndSaveDocument(String title, String text) {
        Document newDoc = documentService.create(title, text);
        newDoc = documentService.save(newDoc);
        requestNotifyObservers(ActionType.ADD, newDoc);
        return newDoc;
    }

    public Optional<Document> get(Long documentId) {
        return documentService.findById(documentId);
    }

    public void delete(Document doc) {
        if (doc != null) {
            documentService.delete(doc);
            requestNotifyObservers(ActionType.DELETE, doc);
        }
    }

    public void requestNotifyObservers() {
        this.setChanged();
        notifyObservers();
    }

    public void requestNotifyObservers(ActionType action, Document doc) {
        this.setChanged();
        notifyObservers(new DocumentAction(action, doc));
    }

    private void requestNotifyObservers(List<Document> savedDocs) {
        for (Document savedDoc : savedDocs) {
            requestNotifyObservers(ActionType.UPDATE, savedDoc);
        }
    }
}

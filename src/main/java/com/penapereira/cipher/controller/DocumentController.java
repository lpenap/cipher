package com.penapereira.cipher.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
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

    protected Configuration config;
    protected DocumentService documentService;
    protected Messages messages;

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
                new StringUtil().listToString(messages.getHelpDocument()));
    }

    public Document save(Document doc) {
        Document savedDoc = documentService.save(doc);
        requestNotifyObservers(ActionType.UPDATE, savedDoc);
        return savedDoc;
    }

    public List<Document> saveAll(List<Document> documents) {
        List<Document> savedDocs = documentService.saveAll(documents);
        requestNotifyObservers(ActionType.UPDATE, savedDocs);
        return savedDocs;
    }

    public Document createAndSaveDocument(String title, String text) {
        Document newDoc = documentService.create(title, text);
        newDoc = documentService.save(newDoc);
        requestNotifyObservers(ActionType.ADD, newDoc);
        return newDoc;
    }

    public Document get(Long documentId) {
        return documentService.findById(documentId);
    }

    public void delete(Long documentId) {
        if (documentId != null) {
            Document doc = documentService.findById(documentId);
            documentService.deleteById(documentId);
            requestNotifyObservers(ActionType.DELETE, doc);
        }
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

    private void requestNotifyObservers(ActionType action, List<Document> savedDocs) {
        Iterator<Document> i = savedDocs.iterator();
        while (i.hasNext()) {
            requestNotifyObservers(action, i.next());
        }
    }
}

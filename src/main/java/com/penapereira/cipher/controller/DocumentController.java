package com.penapereira.cipher.controller;

import java.util.List;
import java.util.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.penapereira.cipher.conf.Configuration;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.model.document.Document;
import com.penapereira.cipher.model.document.DocumentService;
import com.penapereira.cipher.shared.Util;

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
        return documentService.createNew(messages.getHelpDocumentTitle(),
                new Util().listToString(messages.getHelpDocument()));

    }

    public Document save(Document doc) {
        Document savedDoc = documentService.save(doc);
        this.setChanged();
        notifyObservers(savedDoc);
        return savedDoc;
    }

    public Document createDocument(String title, String text) {
        Document newDoc = documentService.createNew(title, text);
        this.setChanged();
        notifyObservers(newDoc);
        return newDoc;
    }
}

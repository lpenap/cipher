package com.penapereira.cipher.model.document;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService {

    protected DocumentRepository documentRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public Document createNew(String title, String text) {
        Document doc = new Document();
        doc.setTitle(title);
        doc.setText(text);
        return documentRepository.save(doc);
    }

    @Override
    public Document save(Document doc) {
        return documentRepository.save(doc);
    }
}

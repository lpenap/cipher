package com.penapereira.cipher.model.document;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService {

    protected final DocumentRepository documentRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public Document create(String title, String text) {
        Document doc = new Document();
        doc.setTitle(title);
        doc.setText(text);
        return doc;
    }

    @Override
    public Document save(Document doc) {
        return documentRepository.save(doc);
    }

    @Override
    public List<Document> saveAll(List<Document> documents) {
        return documentRepository.saveAll(documents);
    }

    @Override
    public Document findById(Long id) {
        return documentRepository.findById(id).get();
    }

    @Override
    public void deleteById(Long id) {
        documentRepository.deleteById(id);
    }

    @Override
    public void delete(Document doc) {
        documentRepository.delete(doc);
    }
}

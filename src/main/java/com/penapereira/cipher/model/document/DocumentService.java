package com.penapereira.cipher.model.document;

import java.util.List;

public interface DocumentService {

    List<Document> findAll();

    Document createNew(String title, String text);

    Document save(Document doc);

    Document findById(Long id);

    void deleteById(Long id);

}

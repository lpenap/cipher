package com.penapereira.cipher.model.document;

import com.penapereira.cipher.CipherApplication;
import com.penapereira.cipher.HeadlessSpringBootContextLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ContextConfiguration(classes = CipherApplication.class, loader = HeadlessSpringBootContextLoader.class)
public class DocumentServiceImplTests {

    @MockBean
    DocumentRepository documentRepositoryMock;

    @Autowired
    private DocumentService documentService;

    @Test
    public void testCreate() {
        String title = "Test Title";
        String text = "Test Text";
        Document doc = new Document();
        doc.setText(text);
        doc.setTitle(title);
        assertEquals(doc, documentService.create(title, text));
    }

    @Test
    public void testFindAll() {
        when(documentRepositoryMock.findAll()).thenReturn(getTestList());
        assertEquals(getTestList(), documentService.findAll());
    }

    @Test
    public void testFindById() {
        Optional<Document> doc = Optional.of(getTestDocument(Long.MAX_VALUE, "test title", "test text"));
        when(documentRepositoryMock.findById(Long.MAX_VALUE)).thenReturn(doc);
        Optional<Document> foundDoc = documentService.findById(Long.MAX_VALUE);
        assertNotNull(foundDoc);
        assertEquals(doc, foundDoc);
    }

    protected List<Document> getTestList() {
        List<Document> testList = new ArrayList<>();
        testList.add(getTestDocument(Long.MIN_VALUE, "titleMin", "textMin"));
        testList.add(getTestDocument(12L, "title02", "text02"));
        testList.add(getTestDocument(55L, "title03", "text03"));
        testList.add(getTestDocument(Long.MAX_VALUE, "titleMax", "textMax"));
        return testList;
    }

    protected Document getTestDocument(Long id, String title, String text) {
        Document doc = new Document();
        doc.setId(id);
        doc.setText(text);
        doc.setTitle(title);
        return doc;
    }
}

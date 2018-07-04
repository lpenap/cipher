package com.penapereira.cipher.model.document;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import com.penapereira.cipher.CipherApplication;
import com.penapereira.cipher.HeadlessSpringBootContextLoader;
import com.penapereira.cipher.model.document.DocumentRepository;
import com.penapereira.cipher.model.document.DocumentServiceImpl;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CipherApplication.class, loader = HeadlessSpringBootContextLoader.class)
public class DocumentServiceImplTests {

    @Mock
    DocumentRepository documentRepositoryMock;

    @InjectMocks
    DocumentServiceImpl documentService;

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
        assertEquals(doc.get(), documentService.findById(Long.MAX_VALUE));
        assertEquals(doc.get(), documentService.findById(Long.MAX_VALUE));
    }

    protected List<Document> getTestList() {
        List<Document> testList = new ArrayList<>();
        testList.add(getTestDocument(Long.MIN_VALUE, "titleMin", "textMin"));
        testList.add(getTestDocument(new Long(12), "title02", "text02"));
        testList.add(getTestDocument(new Long(55), "title03", "text03"));
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

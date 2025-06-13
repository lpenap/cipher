package com.penapereira.cipher;

import com.penapereira.cipher.view.swing.TabbedPaneUserInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.main.headless=true")
@ContextConfiguration(classes = CipherApplication.class)
public class CipherApplicationTests {

    @MockBean
    private TabbedPaneUserInterface ui;

    @Test
    public void contextLoads() {
        assertThat(ui).isNotNull();
    }

}

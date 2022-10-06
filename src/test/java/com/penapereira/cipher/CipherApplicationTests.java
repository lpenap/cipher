package com.penapereira.cipher;

import com.penapereira.cipher.view.swing.TabbedPaneUserInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = CipherApplication.class, loader = HeadlessSpringBootContextLoader.class)
public class CipherApplicationTests {

    @Autowired
    private TabbedPaneUserInterface ui;

    @Test
    public void contextLoads() {
        assertThat(ui).isNotNull();
    }

}

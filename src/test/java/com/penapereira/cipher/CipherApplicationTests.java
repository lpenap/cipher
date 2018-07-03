package com.penapereira.cipher;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import com.penapereira.cipher.view.swing.TabbedPaneUserInterface;

@RunWith(SpringRunner.class)
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

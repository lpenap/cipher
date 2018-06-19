package com.penapereira.cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.penapereira.cipher.view.MainUserInterface;
import com.penapereira.cipher.view.swing.MainUserInterfaceImpl;

@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class CipherApplication {

    private static final Logger log = LoggerFactory.getLogger(CipherApplication.class);

    public static void main(String[] args) {
        log.info("Launching Cipher Application");
        ConfigurableApplicationContext context =
                new SpringApplicationBuilder(CipherApplication.class).headless(false).run(args);

        MainUserInterface userInterface = context.getBean(MainUserInterfaceImpl.class);
        if (!userInterface.init()) {
            System.exit(0);
        }
        userInterface.launch();
    }
}

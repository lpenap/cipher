package com.penapereira.cipher;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class CipherApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                new SpringApplicationBuilder(CipherApplication.class).headless(false).run(args);
        TestFrame appFrame = context.getBean(TestFrame.class);
        appFrame.setVisible(true);
        appFrame.setSize(500, 500);

        appFrame.alert();
    }
}

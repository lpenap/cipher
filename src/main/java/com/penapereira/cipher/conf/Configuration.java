package com.penapereira.cipher.conf;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import lombok.Data;

/**
 * Class for loading application properties and default values.
 * 
 * @author luis
 *
 */
@Component
@ConfigurationProperties(prefix = "cipher")
@Data
public class Configuration {

    private final static Logger log = LoggerFactory.getLogger(Configuration.class);

    private String version;
    private int blockSizeRsa = 117;
    private int kb = 1024;
    private String homeFolder = ".lapcipher";
    private String rsa = "RSA";
    private int rsaKeySize = 2048;
    private String aes = "AES";
    private String aesCbcPkcs5 = "AES/CBC/PKCS5Padding";
    private String privateKeyFile = ".lapcipher_privateKey";
    private String publicKeyFile = ".lapcipher_privateKey";
    private int windowWidth = 900;
    private String documentFont = "Courier";
    private int documentFontSize = 16;

    @EventListener(ApplicationReadyEvent.class)
    public void printConfig() {
        String cipherHomePath = System.getProperty("user.home") + File.separator + this.getHomeFolder();
        log.debug("Cipher home folder: " + cipherHomePath);
    }
}

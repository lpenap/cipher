package com.penapereira.cipher;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

/**
 * Class for loading application properties and default values.
 * @author luis
 *
 */
@Component
@ConfigurationProperties(prefix = "cipher")
@Data
public class Configuration {

    private String dirSep = "/";
    private int blockSizeRsa = 117;
    private int kb = 1024;
    private String homeFolder = "/.lapcipher";
    private String propertiesFile = "cipher.properties.xml";
    private String rsa = "RSA";
    private int rsaKeySize = 2048;
    private String aes = "AES";
    private String aesCbcPkcs5 = "AES/CBC/PKCS5Padding";
    private String privateKeyFile = ".lapcipher_privateKey";
    private String publicKeyFile = ".lapcipher_privateKey";
    private String document = "document.bin";
}

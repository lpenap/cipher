package com.penapereira.cipher.util.cipher;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AESCipherTests {
    @Autowired
    CipherInterface aesCipher;

    @Test
    public void testEncryptDecrypt() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        String inputText = "this is\nan input text";
        String pass = "1234";
        String salt = "salt";

        String cipherText = aesCipher.encrypt(inputText, pass, salt);
        assertNotNull(cipherText);
        assertEquals(inputText, aesCipher.decrypt(cipherText, pass, salt));
    }
}

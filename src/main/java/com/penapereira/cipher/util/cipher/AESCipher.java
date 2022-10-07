package com.penapereira.cipher.util.cipher;

import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Component
public class AESCipher extends AbstractCipher {
    private final String algorithm = "AES/CBC/PKCS5Padding";

    public AESCipher() {
        setIvParameterSpec(generateIv());
    }

    @Override
    public String encrypt(String input, String password, String salt) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, InvalidKeySpecException {
        SecretKey key = getKeyFromPassword(password, salt);
        return encrypt(algorithm, input, key, ivParameterSpec);
    }

    @Override
    public String decrypt(String input, String password, String salt) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, InvalidKeySpecException {
        SecretKey key = getKeyFromPassword(password, salt);
        return decrypt(algorithm, input, key, ivParameterSpec);
    }
}

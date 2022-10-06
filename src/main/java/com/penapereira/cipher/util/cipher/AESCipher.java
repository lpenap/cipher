package com.penapereira.cipher.util.cipher;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class AESCipher extends AbstractCipher {
    private final String algorithm = "AES/CBC/PKCS5Padding";
    private IvParameterSpec ivParameterSpec;

    public AESCipher() {
        ivParameterSpec = generateIv();
    }

    @Override
    public String encrypt(String input, String password, String salt) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, InvalidKeySpecException {
        SecretKey key = getKeyFromPassword(password,salt);
        return encrypt(algorithm, input, key, ivParameterSpec);
    }

    @Override
    public String decrypt(String input, String password, String salt) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, InvalidKeySpecException {
        SecretKey key = getKeyFromPassword(password,salt);
        return decrypt(algorithm, input, key, ivParameterSpec);
    }
}

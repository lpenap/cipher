package com.penapereira.cipher.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.penapereira.cipher.conf.Constants;
import com.penapereira.cipher.model.data.EncryptedDataInterface;
import com.penapereira.cipher.model.data.v1.EncryptedData;

public class AesRsaCipher {

	private Object decryptResultMessage;

	public String decrypt(EncryptedDataInterface cryptedData,
		PrivateKey rsaPrivateKey)
		throws NoSuchAlgorithmException, NoSuchPaddingException,
		InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
		InvalidParameterSpecException, InvalidAlgorithmParameterException,
		IOException {
		decryptResultMessage = null;
		byte[] decryptedText = null;
		ByteArrayOutputStream out = null;
		CipherInputStream in = null;
		ByteArrayInputStream inA = null;

		// Decrypt the session key with the RSA private key
		Cipher rsaCipher = Cipher.getInstance("RSA");
		rsaCipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
		byte[] decryptedSessionKey = rsaCipher
			.doFinal(cryptedData.getSessionKey());
		SecretKey sessionKey = new SecretKeySpec(decryptedSessionKey,
			"AES");

		// init cipher to decrypt the data using the session key
		final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
		params.init(
			new IvParameterSpec(cryptedData.getInitializationVector()));
		cipher.init(Cipher.DECRYPT_MODE, sessionKey, params);

		out = new ByteArrayOutputStream();
		inA = new ByteArrayInputStream(cryptedData.getBytes());
		in = new CipherInputStream(inA, cipher);

		byte[] data = new byte[Constants.KB];
		int read = in.read(data, 0, Constants.KB);
		while (read != -1) {
			out.write(data, 0, read);
			read = in.read(data, 0, Constants.KB);
		}

		out.flush();

		if (in != null) {
			in.close();
		}
		if (inA != null) {
			inA.close();
		}
		if (out != null) {
			out.close();
		}

		decryptedText = out.toByteArray();

		String result = "";
		if (decryptedText != null) {
			try {
				result = new String(decryptedText, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				result = new String(decryptedText);
				decryptResultMessage = "The system does not support UTF-8 !\n"
					+ "The decrypted text has been encoded using\n"
					+ "the default charset: "
					+ Charset.defaultCharset().toString();
			}
		}
		return result;
	}

	public EncryptedDataInterface encrypt(String text, PublicKey rsaPublicKey)
		throws NoSuchAlgorithmException, NoSuchPaddingException,
		InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
		InvalidParameterSpecException, IOException {
		CipherOutputStream cos = null;
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;

		// 1. Generate a session key
		KeyGenerator keyGen;
		keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128);
		SecretKey sessionKey = keyGen.generateKey();

		// 2. Encrypt the session key with the RSA public key

		Cipher rsaCipher = Cipher.getInstance("RSA");
		rsaCipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
		byte[] encryptedSessionKey = rsaCipher
			.doFinal(sessionKey.getEncoded());

		// 3. Encrypt the data using the session key (unencrypted)
		Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		aesCipher.init(Cipher.ENCRYPT_MODE, sessionKey);
		baos = new ByteArrayOutputStream();
		bais = new ByteArrayInputStream(text.getBytes("UTF-8"));
		cos = new CipherOutputStream(baos, aesCipher);

		// 4. saving parameter spec to save initialization vector
		byte[] iv = aesCipher.getParameters()
			.getParameterSpec(IvParameterSpec.class).getIV();

		byte[] data = new byte[Constants.KB];
		int read = bais.read(data, 0, Constants.KB);
		while (read != -1) {
			// System.out.println("Reading " + read + " bytes");
			cos.write(data, 0, read);
			read = bais.read(data, 0, Constants.KB);
		}

		cos.flush();
		baos.flush();

		if (cos != null) {
			cos.close();
		}
		if (baos != null) {
			baos.close();
		}
		if (bais != null) {
			bais.close();
		}
		return new EncryptedData(baos.toByteArray(), encryptedSessionKey,
			iv);
	}
	
	
	public Object getDecryptResultMessage() {
		return decryptResultMessage;
	}

	public void setDecryptResultMessage(Object decryptResultMessage) {
		this.decryptResultMessage = decryptResultMessage;
	}

}

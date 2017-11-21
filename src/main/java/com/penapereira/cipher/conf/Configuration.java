package com.penapereira.cipher.conf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidParameterSpecException;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

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

import com.penapereira.cipher.model.data.EncryptedDataInterface;
import com.penapereira.cipher.model.data.v1.EncryptedData;

public class Configuration {
	private long fileSize = 0;
	protected String documentFile = null;
	protected String decryptMessage = null;

	protected String publicKeyFile = null;
	protected String privateKeyFile = null;

	Properties properties = null;
	protected String propertiesFilename;

	public Configuration() {
	}

	public String decryptAes(EncryptedDataInterface cryptedData,
		PrivateKey rsaPrivateKey)
		throws NoSuchAlgorithmException, NoSuchPaddingException,
		InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
		InvalidParameterSpecException, InvalidAlgorithmParameterException,
		IOException {
		decryptMessage = null;
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
				decryptMessage = "The system does not support UTF-8 !\n"
					+ "The decrypted text has been encoded using\n"
					+ "the default charset: "
					+ Charset.defaultCharset().toString();
			}
		}
		return result;
	}

	public EncryptedDataInterface encryptAes(String text,
		PublicKey rsaPublicKey)
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

	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(obj);
		o.flush();
		o.close();
		return b.toByteArray();
	}

	public static Object deserialize(byte[] bytes)
		throws IOException, ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		return o.readObject();
	}

	public EncryptedDataInterface readFile(String aInputFileName)
		throws ClassNotFoundException, IOException {
		fileSize = 0;
		System.out.println("Reading binary file: " + aInputFileName);
		File file = new File(aInputFileName);
		fileSize = file.length();
		System.out.println("File size: " + file.length() + " bytes");

		byte[] resultBytes = new byte[(int) file.length()];
		InputStream input = null;
		try {
			int totalBytesRead = 0;
			input = new BufferedInputStream(new FileInputStream(file));
			while (totalBytesRead < resultBytes.length) {
				int bytesRemaining = resultBytes.length
					- totalBytesRead;
				int bytesRead = input.read(resultBytes, totalBytesRead,
					bytesRemaining);
				// input.read() returns -1, 0, or more :
				if (bytesRead > 0) {
					totalBytesRead = totalBytesRead + bytesRead;
				}
			}
		} finally {
			if (input != null) {
				input.close();
			}
		}
		return (EncryptedDataInterface) deserialize(resultBytes);
	}

	public void writeFile(EncryptedDataInterface data, String aOutputFileName)
		throws IOException {
		System.out.println("Writing binary file: " + getDocumentFile());
		OutputStream output = null;
		try {
			output = new BufferedOutputStream(
				new FileOutputStream(aOutputFileName));
			output.write(serialize(data));
			output.flush();
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

	public boolean checkFile(String file, String text, PrivateKey privateKey)
		throws InvalidKeyException, NoSuchAlgorithmException,
		NoSuchPaddingException, IllegalBlockSizeException,
		BadPaddingException, InvalidParameterSpecException,
		InvalidAlgorithmParameterException, ClassNotFoundException,
		IOException {
		System.out.println("Verifying file...");
		return decryptAes(readFile(file), privateKey).equals(text);
	}

	public long getFileSize() {
		return fileSize;
	}

	public String getPublicKeyFile() {
		return publicKeyFile;
	}

	public String getPrivateKeyFile() {
		return privateKeyFile;
	}

	public String getDocumentFile() {
		return documentFile;
	}

	public void setDocumentFile(String documentFile) {
		this.documentFile = documentFile;
	}

	public String getDecryptMessage() {
		return decryptMessage;
	}

	public boolean loadConfiguration(String[] args) {
		try {
			properties = loadProperties(getPropertiesPath(args));
			documentFile = properties.getProperty(
				Constants.PROPERTIES_DEFAULT_FILE,
				Constants.DEFAULT_FILE);
			privateKeyFile = properties.getProperty(
				Constants.PROPERTIES_PRIVATE_KEY,
				Constants.DEFAULT_PRIVATE_KEY_FILE);
			publicKeyFile = properties.getProperty(
				Constants.PROPERTIES_PUBLIC_KEY,
				Constants.DEFAULT_PUBLIC_KEY_FILE);
		} catch (InvalidPropertiesFormatException e) {
			System.out.println(
				"Invalid Properties file, please check your file.");
		} catch (FileNotFoundException e) {
			System.out.println("Could not find Properties file.");
		} catch (IOException e) {
			System.out.println("IO Error loading Properties file.");
		}
		return properties != null;
	}

	protected String getPropertiesPath(String[] args) {
		propertiesFilename = Constants.DEFAULT_PROPERTIES_FILE;
		if (args.length >= 1) {
			System.out.println(
				"Will attempt to use properties file: " + args[0]);
			propertiesFilename = args[0];
		} else {
			System.out.println("Using default properties file: "
				+ Constants.DEFAULT_PROPERTIES_FILE);
		}
		return propertiesFilename;
	}

	protected Properties loadProperties(String file)
		throws InvalidPropertiesFormatException, FileNotFoundException,
		IOException {
		Properties properties = null;

		properties = new Properties();
		properties.loadFromXML(new FileInputStream(file));

		Enumeration<Object> enuKeys = properties.keys();
		System.out.println("-------- Properties --------");
		while (enuKeys.hasMoreElements()) {
			String key = (String) enuKeys.nextElement();
			String value = properties.getProperty(key);
			System.out.println("  " + key + ": " + value);
		}
		System.out.println("----------------------------");

		return properties;
	}

	public void loadDefaults() {
		properties = new Properties();
		properties.setProperty(Constants.PROPERTIES_DEFAULT_FILE,
			Constants.DEFAULT_FILE);
		properties.setProperty(Constants.PROPERTIES_PRIVATE_KEY,
			Constants.DEFAULT_PRIVATE_KEY_FILE);
		properties.setProperty(Constants.PROPERTIES_PUBLIC_KEY,
			Constants.DEFAULT_PUBLIC_KEY_FILE);
	}

	public String getFilename() {
		return propertiesFilename;
	}

	public String getProperty(String propertiesKey) {
		return properties.getProperty(propertiesKey);
	}

}

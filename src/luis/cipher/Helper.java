package luis.cipher;

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
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidParameterSpecException;
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

public class Helper {
	private long fileSize = 0;
	protected String publicKeyFile = null;
	protected String privateKeyFile = null;
	protected String documentFile = null;
	protected String decryptMessage = null;

	public Helper(Properties p) {
		p = (p == null) ? new Properties() : p;
		documentFile = p.getProperty(Constants.PROPERTIES_DEFAULT_FILE,
				Constants.DEFAULT_FILE);
		privateKeyFile = p.getProperty(Constants.PROPERTIES_PRIVATE_KEY,
				Constants.DEFAULT_PRIVATE_KEY_FILE);
		publicKeyFile = p.getProperty(Constants.PROPERTIES_PUBLIC_KEY,
				Constants.DEFAULT_PUBLIC_KEY_FILE);
	}

	public String decryptAes(CipherResult cryptedData, PrivateKey rsaPrivateKey)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, InvalidParameterSpecException,
			InvalidAlgorithmParameterException, IOException {
		decryptMessage = null;
		byte[] decryptedText = null;
		ByteArrayOutputStream out = null;
		CipherInputStream in = null;
		ByteArrayInputStream inA = null;

		// Decrypt the session key with the RSA private key
		Cipher rsaCipher = Cipher.getInstance("RSA");
		rsaCipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
		byte[] decryptedSessionKey = rsaCipher.doFinal(cryptedData
				.getEncryptedSessionKey());
		SecretKey sessionKey = new SecretKeySpec(decryptedSessionKey, "AES");

		// init cipher to decrypt the data using the session key
		final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
		params.init(new IvParameterSpec(cryptedData.getIvVector()));
		cipher.init(Cipher.DECRYPT_MODE, sessionKey, params);

		out = new ByteArrayOutputStream();
		inA = new ByteArrayInputStream(cryptedData.getEncryptedData());
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

	public void generateKey() throws NoSuchAlgorithmException, IOException {
		final KeyPairGenerator keyGen = KeyPairGenerator
				.getInstance(Constants.RSA);
		keyGen.initialize(Constants.RSA_KEY_SIZE);
		final KeyPair key = keyGen.generateKeyPair();

		File privateKeyFile = new File(this.privateKeyFile);
		File publicKeyFile = new File(this.publicKeyFile);

		// Create files to store public and private key
		if (privateKeyFile.getParentFile() != null) {
			privateKeyFile.getParentFile().mkdirs();
		}
		privateKeyFile.createNewFile();

		if (publicKeyFile.getParentFile() != null) {
			publicKeyFile.getParentFile().mkdirs();
		}
		publicKeyFile.createNewFile();

		// Saving the Public key in a file
		ObjectOutputStream publicKeyOS = new ObjectOutputStream(
				new FileOutputStream(publicKeyFile));
		publicKeyOS.writeObject(key.getPublic());
		publicKeyOS.close();

		// Saving the Private key in a file
		ObjectOutputStream privateKeyOS = new ObjectOutputStream(
				new FileOutputStream(privateKeyFile));
		privateKeyOS.writeObject(key.getPrivate());
		privateKeyOS.close();
	}

	public boolean areKeysPresent() {

		File privateKey = new File(privateKeyFile);
		File publicKey = new File(publicKeyFile);

		if (privateKey.exists() && publicKey.exists()) {
			return true;
		}
		return false;
	}

	public CipherResult encryptAes(String text, PublicKey rsaPublicKey)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, InvalidParameterSpecException, IOException {
		CipherResult result = null;
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
		byte[] encryptedSessionKey = rsaCipher.doFinal(sessionKey.getEncoded());

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
		result = new CipherResult(baos.toByteArray(), encryptedSessionKey, iv);

		return result;
	}

	public CipherData loadKeys() throws FileNotFoundException,
			ClassNotFoundException, IOException {
		return this.loadKeys(publicKeyFile, privateKeyFile);
	}

	public CipherData loadKeys(String publicKeyFile, String privateKeyFile)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		CipherData result = null;
		ObjectInputStream inputStream = null;

		System.out.print("Loading public key...");
		inputStream = new ObjectInputStream(new FileInputStream(publicKeyFile));
		PublicKey publicKey = (PublicKey) inputStream.readObject();
		inputStream.close();
		System.out.println("Done");

		System.out.print("Loading private key...");
		inputStream = new ObjectInputStream(new FileInputStream(privateKeyFile));
		PrivateKey privateKey = (PrivateKey) inputStream.readObject();
		inputStream.close();
		System.out.println("Done");

		result = new CipherData(privateKey, publicKey);
		return result;
	}

	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(obj);
		o.flush();
		o.close();
		return b.toByteArray();
	}

	public static Object deserialize(byte[] bytes) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		return o.readObject();
	}

	public CipherResult readFile(String aInputFileName)
			throws ClassNotFoundException, IOException {
		fileSize = 0;
		System.out.println("Reading binary file: " + aInputFileName);
		File file = new File(aInputFileName);
		fileSize = file.length();
		System.out.println("File size: " + file.length() + " bytes");
		byte[] resultBytes = new byte[(int) file.length()];
		CipherResult result = null;

		InputStream input = null;
		try {
			int totalBytesRead = 0;
			input = new BufferedInputStream(new FileInputStream(file));
			while (totalBytesRead < resultBytes.length) {
				int bytesRemaining = resultBytes.length - totalBytesRead;
				// input.read() returns -1, 0, or more :
				int bytesRead = input.read(resultBytes, totalBytesRead,
						bytesRemaining);
				if (bytesRead > 0) {
					totalBytesRead = totalBytesRead + bytesRead;
				}
			}
			// System.out.println("Num bytes read: " + totalBytesRead);
			// } catch (ClassNotFoundException e) {
			// fileFound = false;
			// System.out.println("Can not fin class CipherResult Object "
			// + "while reading file.");
			// JOptionPane.showMessageDialog(null, "Could not read object"
			// + " from file!\nCheck log for details.");
		} finally {
			// System.out.println("Closing input stream.");
			if (input != null) {
				input.close();
			}
		}
		result = (CipherResult) deserialize(resultBytes);

		return result;
	}

	public void writeFile(CipherResult data, String aOutputFileName)
			throws IOException {
		System.out.println("Writing binary file: " + getDocumentFile());

		OutputStream output = null;
		try {
			output = new BufferedOutputStream(new FileOutputStream(
					aOutputFileName));
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

	public void setPublicKeyFile(String publicKeyFile) {
		this.publicKeyFile = publicKeyFile;
	}

	public String getPrivateKeyFile() {
		return privateKeyFile;
	}

	public void setPrivateKeyFile(String privateKeyFile) {
		this.privateKeyFile = privateKeyFile;
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

}

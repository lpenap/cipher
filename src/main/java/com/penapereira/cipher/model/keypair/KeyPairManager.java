package com.penapereira.cipher.model.keypair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.penapereira.cipher.conf.Constants;
import com.penapereira.cipher.model.file.FileManager;

public class KeyPairManager {
	private static final Logger logger = LogManager.getLogger();

	protected String publicKeyFile = null;
	protected String privateKeyFile = null;

	KeyPair keyPair = null;

	public KeyPairManager(String publicKeyFile, String privateKeyFile) {
		this.privateKeyFile = privateKeyFile;
		this.publicKeyFile = publicKeyFile;
	}

	public void generateKeyPair()
		throws NoSuchAlgorithmException, IOException {
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
		logger.debug("Looking for keys...");
		File privateKey = FileManager.getResourceFile(getClass(),
			privateKeyFile);
		File publicKey = FileManager.getResourceFile(getClass(),
			publicKeyFile);

		return (privateKey != null && publicKey != null);
	}

	public KeyPair loadKeys()
		throws FileNotFoundException, ClassNotFoundException, IOException {
		this.keyPair = this.loadKeys(publicKeyFile, privateKeyFile);
		return this.keyPair;
	}

	protected Object loadKey(String keyFilename)
		throws IOException, ClassNotFoundException {
		// File file = FileManager.getResourceFile(getClass(), keyFilename);
		File file = new File(keyFilename);
		Object keyObject = null;
		if (file.exists()) {
			logger.debug("Loading key :" + keyFilename);
			FileInputStream in = new FileInputStream(file);
			ObjectInputStream oIn = new ObjectInputStream(in);
			keyObject = oIn.readObject();
			oIn.close();
			in.close();
		} else {
			logger.error("Key file not found: " + keyFilename);
		}
		return keyObject;
	}

	public KeyPair loadKeys(String publicKeyFile, String privateKeyFile)
		throws FileNotFoundException, IOException, ClassNotFoundException {
		logger.debug("Loading public key...");
		PublicKey publicKey = (PublicKey) loadKey(publicKeyFile);
		logger.trace("Done loading public key.");

		logger.debug("Loading private key...");
		PrivateKey privateKey = (PrivateKey) loadKey(privateKeyFile);
		logger.trace("Done loading private key.");

		this.keyPair = new KeyPair(publicKey, privateKey);
		return this.keyPair;
	}

	public KeyPair getKeyPair() {
		return keyPair;
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
}

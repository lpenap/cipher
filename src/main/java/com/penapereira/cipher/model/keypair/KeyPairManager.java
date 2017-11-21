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

import com.penapereira.cipher.conf.Constants;

public class KeyPairManager {
	protected String publicKeyFile = null;
	protected String privateKeyFile = null;

	public KeyPairManager(String publicKeyFile, String privateKeyFile) {
		System.out.println(publicKeyFile);
		System.out.println(privateKeyFile);
		this.privateKeyFile = privateKeyFile;
		this.publicKeyFile = publicKeyFile;
	}

	public void generateKeyPair() throws NoSuchAlgorithmException, IOException {
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

		File privateKey = new File(this.privateKeyFile);
		File publicKey = new File(this.publicKeyFile);

		if (privateKey.exists() && publicKey.exists()) {
			return true;
		}
		return false;
	}

	public KeyPair loadKeys()
		throws FileNotFoundException, ClassNotFoundException, IOException {
		return this.loadKeys(publicKeyFile, privateKeyFile);
	}

	public KeyPair loadKeys(String publicKeyFile, String privateKeyFile)
		throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream inputStream = null;

		System.out.print("Loading public key...");
		inputStream = new ObjectInputStream(
			new FileInputStream(publicKeyFile));
		PublicKey publicKey = (PublicKey) inputStream.readObject();
		inputStream.close();
		System.out.println("Done");

		System.out.print("Loading private key...");
		inputStream = new ObjectInputStream(
			new FileInputStream(privateKeyFile));
		PrivateKey privateKey = (PrivateKey) inputStream.readObject();
		inputStream.close();
		System.out.println("Done");

		return new KeyPair(publicKey, privateKey);
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

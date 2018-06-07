package com.penapereira.cipher.ui.listener;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.penapereira.cipher.model.data.EncryptedDataInterface;
import com.penapereira.cipher.model.file.FileManager;
import com.penapereira.cipher.model.keypair.KeyPairManager;
import com.penapereira.cipher.service.ConfigurationService;
import com.penapereira.cipher.util.AesRsaCipher;

public class SaveActionListener implements ActionListener {
	private static final Logger logger = LogManager.getLogger();
	protected boolean checkAfterSaving = true;

	private JTextPane textPane;
	private KeyPairManager keysManager;
	private ConfigurationService config;
	private FileManager fileManager;
	private Container parentComponent;

	public SaveActionListener(Container parent, JTextPane textPane,
		KeyPairManager keysManager, ConfigurationService config,
		FileManager fileManager) {
		this.textPane = textPane;
		this.parentComponent = parent;
		this.keysManager = keysManager;
		this.config = config;
		this.fileManager = fileManager;
	}

	public void enableCheckAfterSaving(boolean enabled) {
		this.checkAfterSaving = enabled;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (textPane.getText().equals("")) {
			logger.info("Empty text, nothing to save.");
			JOptionPane.showMessageDialog((Component) e.getSource(),
				"Nothing to save.", "",
				JOptionPane.INFORMATION_MESSAGE);
		} else {
			logger.debug("-----------------------------------");
			logger.debug("Encrypting "
				+ textPane.getText().getBytes().length + " bytes");
			EncryptedDataInterface crypted = encrypt(textPane.getText(),
				keysManager.getKeyPair().getPublic());

			if (writeFile(crypted, config.getDocumentFile())) {
				if (checkAfterSaving) {
					try {
						if (checkFile(config.getDocumentFile(),
							textPane.getText())) {
							JOptionPane.showMessageDialog(
								(Component) e.getSource(),
								"File encrypted, saved & verified!",
								"Success",
								JOptionPane.INFORMATION_MESSAGE);
							logger.info("File encrypted, saved & "
								+ "verified!");
						} else {
							logger.error(
								"File could not be saved!");
							JOptionPane.showMessageDialog(
								(Component) e.getSource(),
								"File could not be saved!",
								"Error",
								JOptionPane.ERROR_MESSAGE);
						}
					} catch (Exception e1) {
						logger.error(
							"Error verifying encrypted file: ",
							e1);
						JOptionPane.showMessageDialog(
							(Component) e.getSource(),
							"Error verifying encrypted file!\n"
								+ "Check console log for details.",
							"Warning",
							JOptionPane.WARNING_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(
						(Component) e.getSource(),
						"File encrypted and saved! (not verified)",
						"Success", JOptionPane.INFORMATION_MESSAGE);
					logger.info("File encrypted and saved!"
						+ "(not verified)");
				}
			}
		}
	}

	protected boolean checkFile(String fileName, String text)
		throws InvalidKeyException, NoSuchAlgorithmException,
		NoSuchPaddingException, IllegalBlockSizeException,
		BadPaddingException, InvalidParameterSpecException,
		InvalidAlgorithmParameterException, ClassNotFoundException,
		IOException {
		return fileManager.checkFile(fileName, text,
			keysManager.getKeyPair().getPrivate());
	}

	protected EncryptedDataInterface encrypt(String text,
		PublicKey publicKey) {
		EncryptedDataInterface crypted = null;

		try {
			AesRsaCipher cipher = new AesRsaCipher();
			crypted = cipher.encrypt(textPane.getText(),
				keysManager.getKeyPair().getPublic());
		} catch (Exception e) {
			logger.error("Error encrypting file", e);
			JOptionPane.showMessageDialog(parentComponent,
				"Error encrypting file!"
					+ "check console log for details.",
				"Error", JOptionPane.ERROR_MESSAGE);
		}
		return crypted;
	}

	protected boolean writeFile(EncryptedDataInterface crypted,
		String fileName) {
		boolean result = true;
		try {
			fileManager.writeFile(crypted, fileName);
		} catch (Exception e) {
			logger.error("Error saving file", e);
			JOptionPane.showMessageDialog(parentComponent,
				"Error saving file.\n"
					+ "Check console log for full details.",
				"Error", JOptionPane.ERROR_MESSAGE);
			result = false;
		}
		return result;
	}
}

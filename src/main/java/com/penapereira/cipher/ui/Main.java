package com.penapereira.cipher.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.penapereira.cipher.conf.Configuration;
import com.penapereira.cipher.conf.Constants;
import com.penapereira.cipher.model.data.EncryptedDataInterface;
import com.penapereira.cipher.model.file.FileManager;
import com.penapereira.cipher.model.keypair.KeyPairManager;
import com.penapereira.cipher.ui.listener.SaveActionListener;
import com.penapereira.cipher.util.AesRsaCipher;

public class Main extends JFrame {
	private static final Logger logger = LogManager.getLogger();

	private static final long serialVersionUID = 1L;
	protected JTextPane textPane = null;
	private Configuration config;
	private KeyPair keyPair;
	protected boolean checkAfterSaving = true;
	protected KeyPairManager keysManager;
	protected FileManager fileManager;

	public static void showMessage(Component parent, String text,
		String title, int msgType) {
		logger.debug("showMessage: " + text);
		JOptionPane.showMessageDialog(parent, text, title, msgType);
	}

	public Main(Configuration config, KeyPairManager keysManager,
		FileManager fileManager) {
		super("Editing: "
			+ config.getProperty(Constants.PROPERTIES_DEFAULT_FILE));
		this.keysManager = keysManager;
		this.fileManager = fileManager;
		this.config = config;
	}

	protected void showMainWindow(String text, final PrivateKey privateKey) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton save = new JButton("Encrypt & Save");

		textPane = new JTextPane();
		StyledDocument doc = textPane.getStyledDocument();
		textPane.setFont(new Font("Courier", Font.PLAIN, 14));

		save.addActionListener(new SaveActionListener(this, textPane,
			keysManager, config, fileManager));
		try {
			doc.insertString(doc.getLength(), text,
				doc.getStyle("regular"));
		} catch (BadLocationException e) {
			logger.error("Could not insert text into text pane,"
				+ "continuing may damage the file");
			if (JOptionPane.showConfirmDialog(this,
				"Could not insert the file content into main window\n"
					+ "Check console log for details.\n"
					+ "Do you wish to exit the program to prevent"
					+ "damaging the file?",
				"Error",
				JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION) {
				System.exit(1);
			}
		}
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		add(save, c);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 1.0;
		c.weightx = 1.0;
		JScrollPane scroll = new JScrollPane(textPane);
		add(scroll, c);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int) screenSize.getWidth() / 2,
			(int) screenSize.getHeight() - 100);
		JFrame.setDefaultLookAndFeelDecorated(true);
		setVisible(true);
		textPane.requestFocus();

		if (text.equals("") && fileManager.getFileSize() != 0) {
			JOptionPane.showMessageDialog(this,
				"WARNING! The encrypted file size is "
					+ fileManager.getFileSize()
					+ " bytes\nBut the decrypted text is "
					+ "empty!\nIf you save, the file will be "
					+ "overwritten.",
				"Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void checkKeys() {
		try {
			if (!keysManager.areKeysPresent()) {
				System.out.println(
					"Keys not present, will generate new pair.");
				// TODO there are only 2 options not 3!
				switch (JOptionPane.showConfirmDialog(this,
					"Keys not found :\n" + "private key : "
						+ config.getPrivateKeyFile() + "\n"
						+ "public key : "
						+ config.getPublicKeyFile() + "\n\n"
						+ "Do you want to generate a new pair?:\n"
						+ "Yes : Generate keys in those files.\n"
						+ "No : Will EXIT de program.\n"
						+ "Cancel : Do nothing and continue! "
						+ "(I know what I'm doing!)",
					"Warning", JOptionPane.WARNING_MESSAGE)) {
				case JOptionPane.YES_OPTION:
					System.out.println(
						"Going to generate a new key pair");
					keysManager.generateKeyPair();
					break;
				case JOptionPane.NO_OPTION:
					System.out.println("Exiting.");
					System.exit(0);
					break;
				default:
					System.out.println(
						"Doing nothing, brace yourselves.");
				}
			} else {
				logger.debug("Key files found:");
				logger.debug("  " + config.getPublicKeyFile());
				logger.debug("  " + config.getPrivateKeyFile());
			}
		} catch (IOException e) {
			logger.error(
				"IO Error accessing and/or generating key files, exiting");
			JOptionPane.showMessageDialog(this, "Error accessing and/or "
				+ "generating key pair files! :\n" + "private key : "
				+ config.getPrivateKeyFile() + "\n" + "public key : "
				+ config.getPublicKeyFile() + "\n"
				+ "Will EXIT the program now.", "Error",
				JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		} catch (NoSuchAlgorithmException e) {
			logger.error("RSA algorithm not supporteby the JVM, "
				+ "please check your java installation and try again");
			JOptionPane.showMessageDialog(this,
				"RSA Algorithm not found " + "in the JVM!\n"
					+ "Please check your java installation"
					+ "and try again\n"
					+ "Will EXIT the program now.",
				"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	public KeyPair loadKeys() {
		KeyPair result = null;
		String message = null;
		try {
			result = keysManager.loadKeys();
		} catch (FileNotFoundException e) {
			logger.error("Key files not found!");
		} catch (ClassNotFoundException e) {
			logger.error("Error while trying to load keys. "
				+ "Missing critical files in your installation, aborting.");
			JOptionPane.showMessageDialog(this,
				"Unrecoverablex program error!\n"
					+ "Check your installation or jar file\n"
					+ "Will EXIT the program",
				"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		} catch (IOException e) {
			logger.error("IO Error while trying to load keys.");
		}
		if (result == null) {
			message = "Keys could not be loaded!\n"
				+ "Check the configuration and/or key files.\n"
				+ "Will EXIT the program.";
		} else if (result.getPrivate() == null) {
			message = "Private key could not be loaded!\n"
				+ "Check the configuration and/or key files.\n"
				+ "Will EXIT the program.";
		} else if (result.getPublic() == null) {
			message = "Public key could not be loaded!\n"
				+ "Check the configuration and/or key files.\n"
				+ "Will EXIT the program.";
		}
		if (message != null) {
			JOptionPane.showMessageDialog(this, message, "Error",
				JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return result;
	}

	public EncryptedDataInterface readFile() {
		EncryptedDataInterface data = null;
		try {
			data = fileManager
				.readEncryptedFile(config.getDocumentFile());
		} catch (FileNotFoundException ex) {
			System.out
				.println("File not found: " + config.getDocumentFile());
			System.out.println("Will use file: "
				+ config.getDocumentFile() + " for saving.");
			JOptionPane.showMessageDialog(this,
				"File not found: " + config.getDocumentFile() + "\n"
					+ "Will use this filename for saving.",
				"File not found", JOptionPane.WARNING_MESSAGE);
			// return here to not enter IOException block bellow
			return null;
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("--- End of stack trace");
			System.out.println(
				"Error reading file: " + config.getDocumentFile());
			if (JOptionPane.showConfirmDialog(this,
				"Error reading file: " + config.getDocumentFile() + "\n"
					+ "Click \"Yes\" if you wish to exit the program to "
					+ "prevent damaging the file?",
				"Error",
				JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION) {
				System.exit(1);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("--- End of stack trace");
			System.out.println("Can not find class EncryptedData Object "
				+ "while reading document file.");
			JOptionPane.showMessageDialog(this,
				"Internal Error!\n" + "A class could not be found\n"
					+ "Check console log for details.\n"
					+ "Will EXIT the program now",
				"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		if (data == null) {
			System.out.println(
				"Error reading file: " + config.getDocumentFile());
			if (JOptionPane.showConfirmDialog(this,
				"Error reading file: " + config.getDocumentFile() + "\n"
					+ "Click \"Yes\" if you wish to exit the program to "
					+ "prevent damaging the file?",
				"Error",
				JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION) {
				System.exit(1);
			}
		}
		return data;
	}

	public String decrypt(EncryptedDataInterface data,
		PrivateKey privateKey) {
		String result = null;
		AesRsaCipher cipher = new AesRsaCipher();
		try {
			result = cipher.decrypt(data, privateKey);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.flush();
			System.out.println("--- End of trace.");
			System.out.println(
				"Error decrypting file: " + config.getDocumentFile());
			System.out.println("Maybe the KEYs are wrong or the file is"
				+ " corrupted. Either way, it is recommended "
				+ "to terminate the program and prevent "
				+ "damaging the file.");
			if (JOptionPane.showConfirmDialog(this,
				"Error decrypting file: \n" + config.getDocumentFile()
					+ "\nMaybe the KEYs are wrong or the file is corrupted\n"
					+ "Click \"Yes\" to exit the program and prevent\n"
					+ "damaging the file",
				"Error",
				JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION) {
				System.exit(1);
			}
		}
		if (cipher.getDecryptResultMessage() != null) {
			JOptionPane.showMessageDialog(this,
				cipher.getDecryptResultMessage(), "Warning",
				JOptionPane.WARNING_MESSAGE);
		}
		return result;
	}

	public void initWindow() {
		checkKeys();
		keyPair = loadKeys();
		String text = "";

		EncryptedDataInterface data = readFile();
		if (data != null) {
			text = decrypt(data, keyPair.getPrivate());
		}
		data = null;
		System.gc();

		showMainWindow(text, keyPair.getPrivate());

	}
}

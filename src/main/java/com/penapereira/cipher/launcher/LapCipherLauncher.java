package com.penapereira.cipher.launcher;

import javax.swing.JOptionPane;

import com.penapereira.cipher.conf.Configuration;
import com.penapereira.cipher.conf.Constants;
import com.penapereira.cipher.model.keypair.KeyPairManager;
import com.penapereira.cipher.ui.Main;

public class LapCipherLauncher {
	public static void main(String[] args) {
		Configuration config = new Configuration();
		if (!config.loadConfiguration(args)) {
			Main.printUsage();
			Main.showMessage(null, "There is a problem loading"
					+ " configuration from file:\n" + config.getFilename() + "\n\n"
					+ "Will use defaults:\n\n" + "Document: "
					+ Constants.DEFAULT_FILE + "\n" + "PrivateKey: "
					+ Constants.PROPERTIES_PRIVATE_KEY + "\n" + "PublicKey: "
					+ Constants.PROPERTIES_PUBLIC_KEY, "Warning",
					JOptionPane.WARNING_MESSAGE);
			config.loadDefaults();
		}
		KeyPairManager keysManager= new KeyPairManager(config.getPublicKeyFile(), config.getPrivateKeyFile());
		Main window = new Main(config, keysManager);
		window.initWindow();
	}
}
package com.penapereira.cipher.launcher;

import java.util.Properties;

import javax.swing.JOptionPane;

import com.penapereira.cipher.conf.Constants;
import com.penapereira.cipher.conf.Helper;
import com.penapereira.cipher.conf.PrefsLoader;
import com.penapereira.cipher.ui.Main;

public class LapCipherLauncher {

	public Properties loadPrefs(String file) {
		System.out.println("Loading properties: " + file);
		Properties p = null;
		try {
			p = PrefsLoader.loadPrefs(file);
		} catch (Exception ignored) {
		}
		return p;
	}

	public String checkParams(String[] args) {
		String result = Constants.DEFAULT_PROPERTIES_FILE;
		if (args.length >= 1) {
			System.out.println("Will attempt to use properties file: "
					+ args[0]);
			result = args[0];
		} else {
			System.out.println("Using default properties file: "
					+ Constants.DEFAULT_PROPERTIES_FILE);
		}
		return result;
	}

	public Properties loadDefaults() {
		Properties p = new Properties();
		p.setProperty(Constants.PROPERTIES_DEFAULT_FILE, Constants.DEFAULT_FILE);
		p.setProperty(Constants.PROPERTIES_PRIVATE_KEY,
				Constants.DEFAULT_PRIVATE_KEY_FILE);
		p.setProperty(Constants.PROPERTIES_PUBLIC_KEY,
				Constants.DEFAULT_PUBLIC_KEY_FILE);
		return p;
	}

	public static void main(String[] args) {
		LapCipherLauncher main = new LapCipherLauncher();
		String propertiesFile = main.checkParams(args);
		Properties p = main.loadPrefs(propertiesFile);
		if (p == null) {
			Main.printUsage();
			Main.showMessage(null, "There is a problem loading"
					+ " configuration from file:\n" + propertiesFile + "\n\n"
					+ "Will use defaults:\n\n" + "Document: "
					+ Constants.DEFAULT_FILE + "\n" + "PrivateKey: "
					+ Constants.PROPERTIES_PRIVATE_KEY + "\n" + "PublicKey: "
					+ Constants.PROPERTIES_PUBLIC_KEY, "Warning",
					JOptionPane.WARNING_MESSAGE);
			p = main.loadDefaults();
		}
		Helper helper = new Helper(p);

		Main window = new Main(helper, "Editing: "
				+ p.getProperty(Constants.PROPERTIES_DEFAULT_FILE));

		window.initWindow();

	}

}
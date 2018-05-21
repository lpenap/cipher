package com.penapereira.cipher.launcher;

import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.penapereira.cipher.conf.Configuration;
import com.penapereira.cipher.conf.Constants;
import com.penapereira.cipher.model.file.FileManager;
import com.penapereira.cipher.model.keypair.KeyPairManager;
import com.penapereira.cipher.ui.Main;

public class LapCipherLauncher {

    private final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        LapCipherLauncher launcher = new LapCipherLauncher();
        launcher.loadLauncher(args);
    }

    public void loadLauncher(String[] args) {
        logger.info("Loading launcher...");
        Configuration config = new Configuration();
        if (!config.configurationFolderExists()) {
            config.createDefaultConfiguration();
        }
        if (!config.loadConfiguration(args)) {
            printUsage();
            Main.showMessage(null,
                    "There is a problem loading" + " configuration from file:\n" + config.getFilename() + "\n\n"
                            + "Will use defaults:\n\n" + "Document: " + Constants.DEFAULT_FILE + "\n" + "PrivateKey: "
                            + Constants.PROPERTIES_PRIVATE_KEY + "\n" + "PublicKey: " + Constants.PROPERTIES_PUBLIC_KEY,
                    "Warning", JOptionPane.WARNING_MESSAGE);
            config.loadDefaults();
        }
        KeyPairManager keysManager = new KeyPairManager(config.getPublicKeyFile(), config.getPrivateKeyFile());
        FileManager fileManager = new FileManager();
        Main window = new Main(config, keysManager, fileManager);
        window.initWindow();
    }

    public void printUsage() {
        logger.info("USAGE:");
        logger.info("  java -jar cipher.jar [prop]");
        logger.info("PARAMETERS:");
        logger.info("  prop : properties file to use.");
    }
}

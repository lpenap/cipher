package com.penapereira.cipher.service.impl;

import javax.swing.JOptionPane;
import com.penapereira.cipher.conf.Constants;
import com.penapereira.cipher.model.file.FileManager;
import com.penapereira.cipher.model.keypair.KeyPairManager;
import com.penapereira.cipher.service.ApplicationService;
import com.penapereira.cipher.service.ConfigurationService;
import com.penapereira.cipher.ui.Main;

public class ApplicationServiceImpl implements ApplicationService {

    protected static ApplicationService singletonInstance = null;

    protected ApplicationServiceImpl() {}

    public static ApplicationService instance() {
        if (singletonInstance == null) {
            singletonInstance = new ApplicationServiceImpl();
        }
        return singletonInstance;
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub
        
        
        /**
         *    ConfigurationService config = new ConfigurationService();
        if (!config.configurationExists()) {
            logger.info("ConfigurationServiceImpl folder does not exists...");
            if (uiManager.requestConfigCreationFromUser()) {
                config.createDefaultConfiguration();
            } else {
                uiManager.
                logger.info("Will exit...");
                System.exit(0);
            }
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
         */
        
    }

}

package com.penapereira.cipher.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.penapereira.cipher.conf.Constants;
import com.penapereira.cipher.model.file.FileManager;
import com.penapereira.cipher.service.ConfigurationService;

public class ConfigurationServiceImpl implements ConfigurationService {

    private static final Logger logger = LogManager.getLogger();
    
    protected static ConfigurationService singletonInstance = null;

    protected String documentFile = null;

    protected String publicKeyFile = null;
    protected String privateKeyFile = null;

    Properties properties = null;
    protected String propertiesFilename;

    protected ConfigurationServiceImpl() {
    }
    
    public static ConfigurationService instance() {
        if (singletonInstance == null) {
            singletonInstance = new ConfigurationServiceImpl();
        }
        return singletonInstance;
    }

    @Override
    public String getPublicKeyFile() {
        return publicKeyFile;
    }

    @Override
    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    @Override
    public String getDocumentFile() {
        return documentFile;
    }

    @Override
    public void setDocumentFile(String documentFile) {
        this.documentFile = documentFile;
    }

    @Override
    public boolean loadConfiguration(String[] args) {
        try {
            properties = loadProperties(getPropertiesPath(args));
            documentFile = properties.getProperty(Constants.PROPERTIES_DEFAULT_FILE, Constants.DEFAULT_FILE);
            privateKeyFile =
                    properties.getProperty(Constants.PROPERTIES_PRIVATE_KEY, Constants.DEFAULT_PRIVATE_KEY_FILE);
            publicKeyFile = properties.getProperty(Constants.PROPERTIES_PUBLIC_KEY, Constants.DEFAULT_PUBLIC_KEY_FILE);
        } catch (InvalidPropertiesFormatException e) {
            logger.error("Invalid Properties file, please check your file.");
        } catch (FileNotFoundException e) {
            logger.error("Could not find Properties file.");
        } catch (IOException e) {
            logger.error("IO Error loading Properties file.");
        }
        return properties != null;
    }

    protected String getPropertiesPath(String[] args) {
        propertiesFilename = Constants.DEFAULT_PROPERTIES_FILE;
        if (args.length >= 1) {
            logger.info("Will attempt to use properties file: " + args[0]);
            propertiesFilename = args[0];
        } else {
            logger.info("Using default properties file: " + Constants.DEFAULT_PROPERTIES_FILE);
        }
        return propertiesFilename;
    }

    protected Properties loadProperties(String filename)
            throws InvalidPropertiesFormatException, FileNotFoundException, IOException {
        Properties properties = null;

        properties = new Properties();
        File file = FileManager.getResourceFile(this.getClass(), filename);
        if (file != null) {
            properties.loadFromXML(new FileInputStream(file));
            Enumeration<Object> enuKeys = properties.keys();
            logger.info("-------- Properties --------");
            while (enuKeys.hasMoreElements()) {
                String key = (String) enuKeys.nextElement();
                String value = properties.getProperty(key);
                logger.info("  " + key + ": " + value);
            }
            logger.info("----------------------------");
        }
        return properties;
    }

    @Override
    public void loadDefaults() {
        properties = new Properties();
        properties.setProperty(Constants.PROPERTIES_DEFAULT_FILE, Constants.DEFAULT_FILE);
        properties.setProperty(Constants.PROPERTIES_PRIVATE_KEY, Constants.DEFAULT_PRIVATE_KEY_FILE);
        properties.setProperty(Constants.PROPERTIES_PUBLIC_KEY, Constants.DEFAULT_PUBLIC_KEY_FILE);
    }

    @Override
    public String getFilename() {
        return propertiesFilename;
    }

    @Override
    public String getProperty(String propertiesKey) {
        return properties.getProperty(propertiesKey);
    }

    @Override
    public boolean configurationExists() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setConfigurationDirectory(String optionValue) {
        // TODO Auto-generated method stub
        
    }

}

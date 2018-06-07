package com.penapereira.cipher.service;

public interface ConfigurationService {

    String getPublicKeyFile();

    String getPrivateKeyFile();

    String getDocumentFile();

    void setDocumentFile(String documentFile);

    boolean loadConfiguration(String[] args);

    void loadDefaults();

    String getFilename();

    String getProperty(String propertiesKey);

    boolean configurationExists();

    void setConfigurationDirectory(String optionValue);

}

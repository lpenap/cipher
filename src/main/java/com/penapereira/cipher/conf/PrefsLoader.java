package com.penapereira.cipher.conf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class PrefsLoader {
	public static Properties loadPrefs(String file)
			throws InvalidPropertiesFormatException, FileNotFoundException,
			IOException {
		PrefsLoader loader = new PrefsLoader();
		return loader.doLoad(file);
	}

	protected Properties doLoad(String file)
			throws InvalidPropertiesFormatException, FileNotFoundException,
			IOException {
		Properties properties = null;

		properties = new Properties();
		properties.loadFromXML(new FileInputStream(file));

		Enumeration<Object> enuKeys = properties.keys();
		System.out.println("-------- Properties --------");
		while (enuKeys.hasMoreElements()) {
			String key = (String) enuKeys.nextElement();
			String value = properties.getProperty(key);
			System.out.println("  " + key + ": " + value);
		}
		System.out.println("----------------------------");

		return properties;
	}
}
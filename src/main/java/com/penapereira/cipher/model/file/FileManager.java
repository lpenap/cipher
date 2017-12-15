package com.penapereira.cipher.model.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.penapereira.cipher.conf.Constants;
import com.penapereira.cipher.model.data.EncryptedDataInterface;
import com.penapereira.cipher.util.AesRsaCipher;

public class FileManager {
	private long fileSize = 0;
	private static final Logger logger = LogManager.getLogger();

	public FileManager() {

	}

	public long getFileSize() {
		return fileSize;
	}

	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(obj);
		o.flush();
		o.close();
		return b.toByteArray();
	}

	public static Object deserialize(byte[] bytes)
		throws IOException, ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		return o.readObject();
	}

	public boolean checkFile(String file, String text, PrivateKey privateKey)
		throws InvalidKeyException, NoSuchAlgorithmException,
		NoSuchPaddingException, IllegalBlockSizeException,
		BadPaddingException, InvalidParameterSpecException,
		InvalidAlgorithmParameterException, ClassNotFoundException,
		IOException {
		logger.info("Verifying file...");
		AesRsaCipher cipher = new AesRsaCipher();
		return cipher.decrypt(readEncryptedFile(file), privateKey)
			.equals(text);
	}

	public void writeFile(EncryptedDataInterface data, String outputFileName)
		throws IOException, URISyntaxException {

		File file = getResourceFile(getClass(), outputFileName);
		if (file == null) {
			logger.debug("Resource " + outputFileName
				+ " not found. Creating a new file");
			file = new File(outputFileName);
		}
		OutputStream output = new BufferedOutputStream(
			new FileOutputStream(file));
		output.write(serialize(data));
		output.flush();
		output.close();
	}

	protected String getPathFromJarFile(String jarFilename) {
		return jarFilename.substring(0,
			jarFilename.lastIndexOf(Constants.DIR_SEP) + 1);
	}

	public EncryptedDataInterface readEncryptedFile(String inputFileName)
		throws ClassNotFoundException, IOException {
		logger.info("Reading binary file: " + inputFileName);
		EncryptedDataInterface resultEncryptedData = null;
		File file = getResourceFile(getClass(), inputFileName);
		if (file != null) {
			InputStream inputStream = new FileInputStream(file);
			BufferedInputStream input = new BufferedInputStream(
				inputStream);
			ByteArrayOutputStream resultBytes = new ByteArrayOutputStream();
			int bytesAvailable = input.available();
			while (bytesAvailable > 0) {
				byte[] byteBuffer = new byte[bytesAvailable];
				int bytesRead = input.read(byteBuffer);
				resultBytes.write(byteBuffer, 0, bytesRead);
				bytesAvailable = input.available();
			}
			input.close();
			inputStream.close();
			resultEncryptedData = (EncryptedDataInterface) deserialize(
				resultBytes.toByteArray());
			resultBytes.close();
		} else {
			logger.error("Encrypted file not found: " + inputFileName);
		}

		return resultEncryptedData;
	}

	public static File getResourceFile(
		@SuppressWarnings("rawtypes") Class runtimeClass, String file) {
		String filename;
		File resourceFile = null;
		if (file.startsWith(Constants.DIR_SEP)) {
			filename = file;
		} else {
			filename = "/" + file;
		}

		logger.debug("Trying to find resource: " + filename);
		// testing if we are running from a jar:
		URL resource = runtimeClass.getResource(filename);
		if (resource != null) {
			logger.debug("Resource found: " + resource.getFile());
			if (resource.toString()
				.startsWith(Constants.JAR_RESOURCE_PREFIX)) {
				logger.debug(
					"Resource found inside the jar, skipping...");
				filename = "." + filename;
				File checkFile = new File(filename);
				if (checkFile.exists()) {
					logger.debug(
						"Going to use resource file: " + filename);
					resourceFile = new File(filename);
				}
			} else {
				filename = resource.getFile();
				logger.debug("Going to use resource file: " + filename);
				resourceFile = new File(filename);
			}
		} else {
			logger.error("Resource not found.");
		}
		return resourceFile;
	}
}
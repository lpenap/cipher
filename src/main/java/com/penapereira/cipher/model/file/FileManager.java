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
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.penapereira.cipher.model.data.EncryptedDataInterface;
import com.penapereira.cipher.util.AesRsaCipher;

public class FileManager {
	private long fileSize = 0;

	public FileManager(){
		
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
		System.out.println("Verifying file...");
		AesRsaCipher cipher = new AesRsaCipher();
		return cipher.decrypt(readEncryptedFile(file), privateKey)
			.equals(text);
	}

	public void writeFile(EncryptedDataInterface data, String aOutputFileName)
		throws IOException {
		OutputStream output = null;
		try {
			output = new BufferedOutputStream(
				new FileOutputStream(aOutputFileName));
			output.write(serialize(data));
			output.flush();
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

	public EncryptedDataInterface readEncryptedFile(String inputFileName)
		throws ClassNotFoundException, IOException {
		fileSize = 0;
		System.out.println("Reading binary file: " + inputFileName);
		File file = new File(inputFileName);
		fileSize = file.length();
		System.out.println("File size: " + file.length() + " bytes");

		byte[] resultBytes = new byte[(int) file.length()];
		InputStream input = null;
		try {
			int totalBytesRead = 0;
			input = new BufferedInputStream(new FileInputStream(file));
			while (totalBytesRead < resultBytes.length) {
				int bytesRemaining = resultBytes.length
					- totalBytesRead;
				int bytesRead = input.read(resultBytes, totalBytesRead,
					bytesRemaining);
				// input.read() returns -1, 0, or more :
				if (bytesRead > 0) {
					totalBytesRead = totalBytesRead + bytesRead;
				}
			}
		} finally {
			if (input != null) {
				input.close();
			}
		}
		return (EncryptedDataInterface) deserialize(resultBytes);
	}
}

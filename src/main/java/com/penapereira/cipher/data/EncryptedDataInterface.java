package com.penapereira.cipher.data;

public interface EncryptedDataInterface {

	byte[] getBytes();

	void setBytes(byte[] encryptedData);

	byte[] getSessionKey();

	void setSessionKey(byte[] encryptedSessionKey);

	byte[] getInitializationVector();

	void setInitializationVector(byte[] ivVector);

}
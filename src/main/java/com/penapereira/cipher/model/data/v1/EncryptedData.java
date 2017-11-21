package com.penapereira.cipher.model.data.v1;

import java.io.Serializable;

import com.penapereira.cipher.model.data.EncryptedDataInterface;

public class EncryptedData implements Serializable, EncryptedDataInterface {
	protected static final long serialVersionUID = 1L;
	protected byte[] encryptedData;
	protected byte[] encryptedSessionKey;
	protected byte[] initializationVector;

	public EncryptedData() {

	}

	public EncryptedData(byte[] encryptedData, byte[] encryptedSessionKey,
		byte[] initializationVector) {
		this.encryptedData = encryptedData;
		this.encryptedSessionKey = encryptedSessionKey;
		this.initializationVector = initializationVector;
	}

	public byte[] getBytes() {
		return encryptedData;
	}

	public void setBytes(byte[] encryptedData) {
		this.encryptedData = encryptedData;
	}

	public byte[] getSessionKey() {
		return encryptedSessionKey;
	}

	public void setSessionKey(byte[] encryptedSessionKey) {
		this.encryptedSessionKey = encryptedSessionKey;
	}

	public byte[] getInitializationVector() {
		return initializationVector;
	}

	public void setInitializationVector(byte[] ivVector) {
		initializationVector = ivVector;
	}
}

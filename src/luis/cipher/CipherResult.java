package luis.cipher;

import java.io.Serializable;

public class CipherResult implements Serializable {
	private static final long serialVersionUID = 1L;
	protected byte[] encryptedData;
	protected byte[] encryptedSessionKey;
	protected byte[] IvVector;

	public CipherResult() {

	}

	public CipherResult(byte[] encryptedData, byte[] encryptedSessionKey, byte[] IvVector) {
		this.encryptedData = encryptedData;
		this.encryptedSessionKey = encryptedSessionKey;
		this.IvVector = IvVector;
	}

	public byte[] getEncryptedData() {
		return encryptedData;
	}

	public void setEncryptedData(byte[] encryptedData) {
		this.encryptedData = encryptedData;
	}

	public byte[] getEncryptedSessionKey() {
		return encryptedSessionKey;
	}

	public void setEncryptedSessionKey(byte[] encryptedSessionKey) {
		this.encryptedSessionKey = encryptedSessionKey;
	}

	public byte[] getIvVector() {
		return IvVector;
	}

	public void setIvVector(byte[] ivVector) {
		IvVector = ivVector;
	}
}

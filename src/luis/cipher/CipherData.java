package luis.cipher;

import java.security.PrivateKey;
import java.security.PublicKey;

public class CipherData {
	private PrivateKey privateKey = null;

	private PublicKey publicKey = null;

	public CipherData() {

	}

	public CipherData(PrivateKey privateKey, PublicKey publicKey) {
		this.privateKey = privateKey;
		this.publicKey = publicKey;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}
}

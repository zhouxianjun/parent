package com.gary.util.code.dh;

import java.io.Serializable;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.gary.util.code.Coder;

public class KeyDto extends Coder implements Serializable{

	private static final long serialVersionUID = -8168557964883236009L;

	public KeyDto() {
		// TODO Auto-generated constructor stub
	}
	
	public KeyDto(PublicKey publicKey, PrivateKey privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
	
	private PublicKey publicKey;
	
	private PrivateKey privateKey;

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	
	public String getString(Key key) throws Exception{
		return encryptBASE64(key.getEncoded());
	}
	
	public String toPublicKey() throws Exception{
		return encryptBASE64(this.publicKey.getEncoded());
	}
	
	public String toPrivateKey() throws Exception{
		return encryptBASE64(this.privateKey.getEncoded());
	}
}

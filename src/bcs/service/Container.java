package bcs.service;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;

public final class Container implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7466529511131233182L;
	private byte[] content ; 
	
	public Container(PublicKey publicKey , double amount ) throws ContainerException {
		this.content = close(publicKey, amount);
	}
	
	public String open(PrivateKey privateKey) throws ContainerException {
		if(KeysGenerator.decrypt(this.content, privateKey) != null)
			return new String(KeysGenerator.decrypt(this.content, privateKey));
		else
			throw new ContainerException("open");
	}
	
	private byte[] close(PublicKey publicKey, double amount) throws ContainerException{
		if( KeysGenerator.encrypt(Double.toString(amount), publicKey) != null )
			return KeysGenerator.encrypt(Double.toString(amount), publicKey);
		else
			throw new ContainerException("close");
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new String(this.content) ;
	}
	
	
}

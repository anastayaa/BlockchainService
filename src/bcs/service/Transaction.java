package bcs.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class Transaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7239862875910380757L;
	private final String toAddress ;
	private final String fromAddress;
	private final Container amount; 
	private final String signature ;
	private final Timestamp timestamp;
	public Transaction(Container amount, String toAddress, String fromAddress, Timestamp timestamp) {
		this.fromAddress = fromAddress ;
		this.toAddress =  toAddress;
		this.amount = amount;
		this.timestamp = timestamp ;
		this.signature = this.signatureTransaction();
	}
	
	private String signatureTransaction() {
		if(this.isValid()) {
			return SHA.sha256(this.toAddress + this.fromAddress + this.amount.toString() + this.timestamp.toString()).toString();
		}
		return null;
	}
	
	private boolean isValid() {
		if (this.toAddress == null || this.fromAddress == null ) {
			return false ;
		}
		return true;
	}
	
	public String getSignature() {
		return this.signature ; 
	}
	
	public boolean isValidTransaction() {
		if (this.isValid()) {
			if (this.signature != null && this.signature.equalsIgnoreCase( SHA.sha256(this.toAddress + this.fromAddress + this.amount.toString() + this.timestamp.toString()).toString())) {
				return true ;
			}
		}
		return false; 
	}
	
	
	public Container getContainer() {
		return this.amount;
	}
	
	public String getToAddress() {
		return this.toAddress;
	}
	
	public String getFromAddress() {
		return this.fromAddress;
	}
	
	@Override
	public String toString() {
		return "{\nfromAddress : " + this.fromAddress+",\ntoAddress : " + this.toAddress + ",\namount : " + this.amount.toString() + ",\nTimestamp : " + this.timestamp.toString() + ",\nsignature : "+this.signature+"\n}" ;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}
}

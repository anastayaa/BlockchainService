package bcs.service;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Block implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3359498970885825984L;
	private ArrayList<Transaction> transactions ;
	private final Timestamp timestamp ;
	private final String previoushash ;
	private long nonce ;
	private final String hashId ;
	
	
	
	public Block(Timestamp timestamp, ArrayList<Transaction> transactions, String previousHash) {
		this.timestamp = timestamp;
		this.previoushash = previousHash;
		this.transactions = transactions;
		this.nonce =0 ;
		this.hashId = this.calculateHashId();
	}
	
	
	private String calculateHashId() {
		String allTransaction = null;
		for(Transaction t: transactions)
			allTransaction+=t;
		return SHA.sha256(this.timestamp.toString()+this.nonce+allTransaction+this.previoushash).toString();
	}
	
	
	public boolean hasValidTransaction() {
		for( Transaction t: transactions) 
			if(!t.isValidTransaction())
				return false ;
		return true ;
	}


	public Timestamp getTimestamp() {
		return timestamp;
	}


	public long getNonce() {
		return nonce;
	}
	
	public String getPrevioushash() {
		return previoushash;
	}



	public String getHashId() {
		return hashId;
	}
	
	public ArrayList<Transaction> getTransactions() {
		return transactions;
	}
	
//	@Override
//	public String toString() {
//		String trs = this.transactions.get(0).toString() ;
//		for(int i = 1 ; i < this.transactions.size()  ;  i++ )
//			trs+=",\n"+this.transactions.get(i).toString();
//
//		return "{\nTimestamp : "+this.timestamp.toString()+",\npreviousHash : "+this.previoushash + ",\nnonce : "+this.nonce + ",\nhashId : "+this.hashId + ",\nTransactions : ["+trs+"]\n}" ;
//	}
//	
	
	
	
	
	
}

package sys.user;



import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;

import bcs.service.Block;
import bcs.service.Blockchain;
import bcs.service.Container;
import bcs.service.ContainerException;
import bcs.service.InvalidTransactionException;
import bcs.service.KeysGenerator;
import bcs.service.Transaction;
import pubsub.dp.Ledger;
import pubsub.dp.LedgerFailedException;



public final class Node implements Serializable{

	
	private static final long serialVersionUID = 6502316170052669437L;
	private KeysGenerator keys ;
	private double balance ;
	private String address ;
	private Ledger ledger = Ledger.getInstance();
	private final PublicKey publicKey ;
	
	
	
	public Node(double balance, String address) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, ClassNotFoundException {
		this.balance = balance;
		this.address = address ;
		this.keys = new KeysGenerator(2048);
		this.publicKey = this.keys.getPublicKey();
		
	}
	
	
	
	public PublicKey getPublicKey() {
		return this.publicKey;
	}
	
	
	
	public String getAddress() {
		return this.address; 
	}
	
	
	
	
	public void sendTransaction(String toAddress, double amount) throws AmountException, WalletException, ContainerException, LedgerFailedException, AccountNotFoundException, InvalidTransactionException {
		if(!this.getAddress().equalsIgnoreCase(toAddress)) {
			if(this.balance - amount>=0){
				Node toNode = this.ledger.getNodes().get(toAddress);
				if(toNode != null) {
					Container container = new Container(toNode.getPublicKey(), amount);
					Transaction transaction = new Transaction(container, toAddress, this.address, new Timestamp(System.currentTimeMillis()));
					this.ledger.publish(transaction);
				}else
					throw new AccountNotFoundException();
			}else
				throw new AmountException();
		}else
			throw new WalletException(toAddress);
	}
	
	
	public void receiveBlockchain(Blockchain blockchain) {
		
	}

	public void recieve(Transaction transaction) throws ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException, ContainerException, IOException {
		Container container = transaction.getContainer();
		String content = container.open(this.keys.getPrivateKey());
		double amount =  Double.parseDouble(content);
		this.balance+=amount;
	}
	
	public void notifyMethod(Block block) {
		
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	
	
	
	
	
}

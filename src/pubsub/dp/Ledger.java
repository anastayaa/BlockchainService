package pubsub.dp;



import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import bcs.service.Block;
import bcs.service.Blockchain;
import bcs.service.InvalidTransactionException;
import bcs.service.SHA;
import bcs.service.Transaction;
import sys.user.AccountNotFoundException;
import sys.user.Node;

public final class Ledger implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2592974705538361095L;
	private final Blockchain blockchain = Blockchain.getInstance();
	private static Ledger instance = null;
	private final HashMap<String, Node> nodes ;
	private ArrayList<Transaction> transactions = new ArrayList<>();
	
	
	private Ledger() {
		this.nodes = new HashMap<>();
	}
	
	
	public static Ledger getInstance() {
		if(Ledger.instance == null)
			Ledger.instance = new Ledger();
		return Ledger.instance;
	}
	
	
	public Node addNode() throws NoSuchAlgorithmException, InvalidKeySpecException, ClassNotFoundException, IOException  {
		String createAddress = SHA.sha256(new Timestamp(System.currentTimeMillis()).toString()).toString();
		Node node = new Node(0, createAddress);
		this.nodes.put(createAddress, node);
		return node ;
	}
	
	
	
	public boolean checkExistingNodeByAddress(String address) {
		Node node = this.nodes.get(address);
		if( node != null) 
			return true;
		return false;
	}


	public void publish(Transaction transaction) throws LedgerFailedException, AccountNotFoundException, InvalidTransactionException {
		if(transaction.isValidTransaction()) {
			this.transactions.add(transaction);
			String toAddress = transaction.getToAddress();
			Node toNode = this.nodes.get(toAddress);
			if(toNode != null) {
				try {
					toNode.recieve(transaction);
				} catch (Exception e) {
					throw new LedgerFailedException("publish");
				}
			}else
				throw new AccountNotFoundException();
		}else
			throw new InvalidTransactionException();
	}
	
	public boolean miningBlock() {
		if(this.transactions.size() > 3) {
			Block block = new Block(new Timestamp(System.currentTimeMillis()), this.transactions, this.blockchain.getLastBlock().getHashId());
			this.blockchain.addBlock(block);
			return true ;
		}
		return false ;
	}
	
	public Blockchain getBlockchain() {
		return this.blockchain;
	}
	
	public HashMap<String, Node> getNodes() {
		return nodes;
	}
}

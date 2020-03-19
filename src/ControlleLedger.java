import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

import bcs.service.InvalidTransactionException;
import bcs.service.Transaction;
import pubsub.dp.Ledger;
import pubsub.dp.LedgerFailedException;
import pubsub.dp.RemoteLedger;
import sys.user.AccountNotFoundException;
import sys.user.Node;

public class ControlleLedger extends UnicastRemoteObject implements RemoteLedger {

	private static final long serialVersionUID = 2315262487187036665L;
	private Ledger ledger ;
	
	
	protected ControlleLedger() throws RemoteException {
		super();
		this.ledger = Ledger.getInstance();
		
		
	}

	@Override
	public Node createAccount() throws LedgerFailedException {
		
		try {
			return this.ledger.addNode();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | ClassNotFoundException | IOException e) {
			System.out.println("Cannot create user !!");
		}
		return null ;
	}

	@Override
	public void createTransaction(Transaction transaction) throws LedgerFailedException, AccountNotFoundException, InvalidTransactionException {
		if(transaction != null) 
			this.ledger.publish(transaction);
		else
			throw new LedgerFailedException("add new transaction");
		
	}

	@Override
	public HashMap<String, Node> getNodes() throws RemoteException {
		
		return this.ledger.getNodes();
	}

}

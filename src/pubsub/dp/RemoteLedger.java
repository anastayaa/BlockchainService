package pubsub.dp;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

import bcs.service.InvalidTransactionException;
import bcs.service.Transaction;
import sys.user.AccountNotFoundException;
import sys.user.Node;

public interface RemoteLedger extends Remote {

	Node createAccount() throws LedgerFailedException , RemoteException ;
	void createTransaction(Transaction transaction)  throws LedgerFailedException, AccountNotFoundException, InvalidTransactionException, RemoteException ;
	HashMap< String , Node > getNodes() throws RemoteException;
	
}

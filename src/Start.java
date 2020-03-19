import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.HashMap;

import bcs.service.InvalidTransactionException;
import bcs.service.Transaction;
import pubsub.dp.Ledger;
import pubsub.dp.LedgerFailedException;
import pubsub.dp.RemoteLedger;
import pubsub.dp.ServerErrors;
import pubsub.dp.ServerSuccess;
import sys.user.AccountNotFoundException;
import sys.user.Node;


public class Start extends Thread {

	@Override
	public void run() {
		Log("server", "Start ... ");
		ServerSocket ss = null ;
		try {
			ss = new ServerSocket(1234);
			
			while(true) {
				Socket socket = ss.accept();
				new ClientHandler(socket).start();
			}
			
		} catch (IOException e) {
			
			Log("server", "SERVER FAILED");
		}finally {
			if(ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					System.out.println(":'(");
				}
			}
		}
	}
	
	
	private static class ClientHandler extends Thread implements RemoteLedger{
		
		private Socket socket ;
		
		public ClientHandler(Socket socket) {
			this.socket = socket;
		}
		
		
		@Override
		public void run() {
			Log("server", "user trying to connect...");
			try {
				// etat Critique 
				ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
				Node node = (Node) ois.readObject();
				
				if(Ledger.getInstance().checkExistingNodeByAddress(node.getAddress())) {
					oos.writeInt(ServerSuccess.SUCCESS_EXIST_USER);
					Log("user", "SUCCESS_EXIST_USER");
				}
				
				else {
					oos.writeInt(ServerErrors.ERROR_USER_NOT_FOUND);
					Log("user", "ERROR_USER_NOT_FOUND");
					
				}
				// fin etat critique 
				while(true) {
					//etat critique 
					Transaction transaction = (Transaction) ois.readObject();	
					try {
						this.createTransaction(transaction);
						oos.writeInt(ServerSuccess.SUCCESS_CREATE_TRANSACTION);
						Log("transaction", "SUCCESS_CREATE_TRANSACTION");
					} catch (LedgerFailedException e) {
						oos.writeInt(ServerErrors.ERROR_FAILED_CREATE_TRANSACTION);
						Log("transaction", "ERROR_FAILED_CREATE_TRANSACTION");
					} catch (AccountNotFoundException e) {
						oos.writeInt(ServerErrors.ERROR_USER_NOT_FOUND);
						Log("user", "ERROR_USER_NOT_FOUND");
					} catch (InvalidTransactionException e) {
						oos.writeInt(ServerErrors.ERROR_TRANSACTION);
						Log("transaction", "ERROR_TRANSACTION");
					}
				
					//fin etat critique 
					
				}
				
				
			} catch (IOException | ClassNotFoundException e) {
				Log("server", "FATAL_ERROR");
			}
		}

		@Override
		synchronized public Node createAccount() throws LedgerFailedException {
			Node node = null ;
			try {
				node = Ledger.getInstance().addNode();
			} catch (Exception e) {
				throw new LedgerFailedException("create new node");
			}
			return node ;
		}

		@Override
		synchronized public void createTransaction(Transaction transaction) throws LedgerFailedException, AccountNotFoundException, InvalidTransactionException {
			if(transaction != null) 
				Ledger.getInstance().publish(transaction);
			else
				throw new LedgerFailedException("add new transaction");
		}


		@Override
		public HashMap<String, Node> getNodes() throws RemoteException {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public static void Log(String id , String msg) {
		System.err.print( id + "\t");
		System.out.println(msg);
	}
	
	public static void main(String[] args) {
		new Start().start();
	}
	

}
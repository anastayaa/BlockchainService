import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import pubsub.dp.LedgerFailedException;
import pubsub.dp.RemoteLedger;

public class TestClientRMI {

	
	public static void main(String[] args) {
		
		
		try {
			RemoteLedger rl = (RemoteLedger) Naming.lookup("rmi://localhost:1099/ledger");
			rl.createAccount();
			rl.getNodes().forEach((k,v)-> { System.out.println(v.getAddress()); });
		} catch (MalformedURLException | RemoteException | NotBoundException | LedgerFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

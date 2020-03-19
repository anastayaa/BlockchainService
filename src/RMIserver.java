import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RMIserver {

	public static void main(String[] args) {
		
		try {
			
			LocateRegistry.createRegistry(1099);
			ControlleLedger controlleLedger = new ControlleLedger();
			System.out.println(controlleLedger.toString());
			Naming.rebind("rmi://localhost/ledger", controlleLedger);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

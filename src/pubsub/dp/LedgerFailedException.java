package pubsub.dp;

public class LedgerFailedException extends Exception {

	
	private static final long serialVersionUID = -1862909617009923759L;

	public LedgerFailedException(String job) {
		System.err.println("Ledger failed to "+ job);
	}
}

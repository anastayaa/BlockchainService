package sys.user;

public class WalletException extends Exception {

	
	private static final long serialVersionUID = 1L;

	public WalletException(String address) {
		System.err.println("Cannot send amount to address : " + address);
	}
}

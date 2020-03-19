package sys.user;

public class AccountNotFoundException extends Exception {

	
	private static final long serialVersionUID = 1L;

	public AccountNotFoundException() {
		System.err.println("this Address doesn\'t exist ");
	}
}

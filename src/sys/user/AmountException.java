package sys.user;

public class AmountException extends Exception {

	
	private static final long serialVersionUID = 1L;

	public AmountException() {
		System.err.println("You don\'t have that much money in your account");
	}
}

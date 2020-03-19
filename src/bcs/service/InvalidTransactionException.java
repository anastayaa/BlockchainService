package bcs.service;

public class InvalidTransactionException extends Exception {


	private static final long serialVersionUID = 1L;

	public InvalidTransactionException() {
		System.err.println("Invalid transaction ");
	}
}

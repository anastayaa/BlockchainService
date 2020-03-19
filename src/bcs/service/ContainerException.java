package bcs.service;

public class ContainerException extends Exception {

	private static final long serialVersionUID = 1L;

	public ContainerException(String method) {
		System.err.println("Error in method :" + method);
	}
	
}

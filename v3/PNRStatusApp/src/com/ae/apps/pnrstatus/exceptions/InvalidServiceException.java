package com.ae.apps.pnrstatus.exceptions;

/**
 * This exception is thrown when a requested Service type is not identified by the ServiceFactory.
 * 
 * @author Midhun_Harikumar
 * 
 */
public class InvalidServiceException extends java.lang.Exception {

	/**
	 * serialVersionUID
	 */
	private static final long	serialVersionUID	= 7374739211962780312L;

	public InvalidServiceException() {
		super("The requested Service name is invalid.");
	}

	public InvalidServiceException(String detailMessage) {
		super(detailMessage);
	}

	public InvalidServiceException(Throwable throwable) {
		super(throwable);
	}

	public InvalidServiceException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

}

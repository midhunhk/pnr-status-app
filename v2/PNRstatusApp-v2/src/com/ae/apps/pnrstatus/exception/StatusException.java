package com.ae.apps.pnrstatus.exception;

public class StatusException extends Exception {

	private static final long serialVersionUID = 4678860372933762653L;
	
	private String statusString;

	public StatusException() {
		super();
	}

	public StatusException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public StatusException(String detailMessage) {
		super(detailMessage);
	}

	public StatusException(Throwable throwable) {
		super(throwable);
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	public String getStatusString() {
		return statusString;
	}

}

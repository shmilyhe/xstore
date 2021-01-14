package io.shmilyhe.xmlstore.exception;

public class CrudException extends RuntimeException {

	private static final long serialVersionUID = 924172232565673373L;

	public CrudException(String message) {
		super(message);
	}

	public CrudException(String message, Throwable cause) {
		super(message, cause);
	}
}
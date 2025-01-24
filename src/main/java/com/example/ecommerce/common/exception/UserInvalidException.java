package com.example.ecommerce.common.exception;

public class UserInvalidException extends RuntimeException {
	private static final long serialVersionUID = -526313233133876822L;

	public UserInvalidException() {
		super();
	}

	public UserInvalidException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserInvalidException(String message) {
		super(message);
	}

	public UserInvalidException(Throwable cause) {
		super(cause);
	}
}
package com.example.ecommerce.common.exception;

/**
 * for data validation errors while trying create/update objects
 */
public final class CustomAuthenticationException extends RuntimeException {
	private static final long serialVersionUID = -6163112577637316727L;

	public CustomAuthenticationException() {
		super();
	}

	public CustomAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomAuthenticationException(String message) {
		super(message);
	}

	public CustomAuthenticationException(Throwable cause) {
		super(cause);
	}
}
package com.example.ecommerce.common.exception;

/**
 * for data validation errors while trying create/update objects
 */
public final class AccountSuspendedException extends RuntimeException {
	private static final long serialVersionUID = -6163523177637316727L;

	public AccountSuspendedException() {
		super();
	}

	public AccountSuspendedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccountSuspendedException(String message) {
		super(message);
	}

	public AccountSuspendedException(Throwable cause) {
		super(cause);
	}
}
package com.example.ecommerce.common.exception;

/**
 * for HTTP 404 errors
 */
public final class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -2163119577637316727L;

	public ResourceNotFoundException() {
		super();
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(Throwable cause) {
		super(cause);
	}
}
package com.example.ecommerce.common.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * for data validation errors while trying create/update objects
 */
public final class DataValidationException extends RuntimeException {
	private static final long serialVersionUID = -526313233133876822L;
	private Map<String, Object> data = new HashMap<>();

	public Map<String, Object> getData() {
		return data;
	}

	public DataValidationException() {
		super();
	}

	public DataValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataValidationException(String message) {
		super(message);
	}
	public DataValidationException(String message, Map<String, Object> data) {
		super(message);
		this.data = data;
	}

	public DataValidationException(Throwable cause) {
		super(cause);
	}
}
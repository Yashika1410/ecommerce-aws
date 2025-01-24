package com.example.ecommerce.common.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * for data validation errors while trying create/update objects
 */
public final class PermissionDeniedException extends RuntimeException {
	private static final long serialVersionUID = -526313233186876822L;
	private Map<String, Object> data = new HashMap<>();

	public Map<String, Object> getData() {
		return data;
	}

	public PermissionDeniedException() {
		super();
	}

	public PermissionDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

	public PermissionDeniedException(String message) {
		super(message);
	}
	public PermissionDeniedException(String message, Map<String, Object> data) {
		super(message);
		this.data = data;
	}

	public PermissionDeniedException(Throwable cause) {
		super(cause);
	}
}
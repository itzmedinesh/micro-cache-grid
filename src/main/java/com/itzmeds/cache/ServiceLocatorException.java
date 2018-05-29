package com.itzmeds.cache;

@SuppressWarnings("serial")
public class ServiceLocatorException extends Exception {

	public ServiceLocatorException() {
	}

	public ServiceLocatorException(String message) {
		super(message);
	}

	public ServiceLocatorException(Throwable cause) {
		super(cause);
	}

	public ServiceLocatorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceLocatorException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

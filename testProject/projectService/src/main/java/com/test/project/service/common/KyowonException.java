package com.test.project.service.common;

@SuppressWarnings("serial")
public class KyowonException extends Exception {
    public KyowonException(Throwable cause) {
        super(cause);
    }

    public KyowonException(String message, Throwable cause) {
        super(message, cause);
    }

    public KyowonException(String message) {
        super(message);
    }

    public KyowonException() {}
}

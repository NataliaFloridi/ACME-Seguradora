package com.acme.seguradora.domain.exception;


public class QuotePersistenceException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public QuotePersistenceException(String message) {
        super(message);
    }

    public QuotePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}

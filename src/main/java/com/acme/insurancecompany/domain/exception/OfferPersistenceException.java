package com.acme.insurancecompany.domain.exception;

public class OfferPersistenceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OfferPersistenceException(String message) {
        super(message);
    }

    public OfferPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}

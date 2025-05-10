package com.example.demo.Exceptions;

public class GenderFetchException extends RuntimeException {
    public GenderFetchException(String message) {
        super(message);
    }

    public GenderFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}

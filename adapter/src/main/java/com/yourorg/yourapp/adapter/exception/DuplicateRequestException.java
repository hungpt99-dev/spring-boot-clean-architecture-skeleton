package com.yourorg.yourapp.adapter.exception;

public class DuplicateRequestException extends RuntimeException {
    public DuplicateRequestException(String key) {
        super("Duplicate request for key: " + key);
    }
}


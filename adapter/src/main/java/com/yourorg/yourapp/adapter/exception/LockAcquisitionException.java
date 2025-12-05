package com.yourorg.yourapp.adapter.exception;

public class LockAcquisitionException extends RuntimeException {
    public LockAcquisitionException(String lockName) {
        super("Could not acquire lock: " + lockName);
    }
}


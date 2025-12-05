package com.yourorg.yourapp.adapter.exception;

public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(String bucket) {
        super("Rate limit exceeded for: " + bucket);
    }
}


package com.yourorg.yourapp.adapter.exception;

public class CaptchaFailedException extends RuntimeException {
    public CaptchaFailedException(String msg) {
        super(msg);
    }
}


package com.yourorg.yourapp.adapter.exception;

public class FeatureDisabledException extends RuntimeException {
    public FeatureDisabledException(String feature) {
        super("Feature disabled: " + feature);
    }
}


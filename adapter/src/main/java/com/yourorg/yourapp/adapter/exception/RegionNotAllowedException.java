package com.yourorg.yourapp.adapter.exception;

public class RegionNotAllowedException extends RuntimeException {
    public RegionNotAllowedException(String region) {
        super("Region not allowed: " + region);
    }
}


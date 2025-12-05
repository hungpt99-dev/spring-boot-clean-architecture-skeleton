package com.yourorg.yourapp.adapter.exception;

public class TimeWindowViolationException extends RuntimeException {
    public TimeWindowViolationException(String start, String end, String zone) {
        super("Outside allowed time window: " + start + " - " + end + " (" + zone + ")");
    }
}


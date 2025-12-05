package com.yourorg.yourapp.adapter.web;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(Instant timestamp, String message, List<String> details) {

    public static ApiErrorResponse of(String message, List<String> details) {
        return new ApiErrorResponse(Instant.now(), message, details);
    }
}


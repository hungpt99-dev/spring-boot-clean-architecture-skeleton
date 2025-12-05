package com.yourorg.yourapp.adapter.web.dto;

import java.time.Instant;

/**
 * Generic API response wrapper to standardize success/failure payloads.
 */
public record ApiResponse<T>(String status, String message, T data, Instant timestamp) {

    public static <T> ApiResponse<T> success(String message, T data, Instant timestamp) {
        return new ApiResponse<>("SUCCESS", message, data, timestamp);
    }

    public static <T> ApiResponse<T> failure(String message, T data, Instant timestamp) {
        return new ApiResponse<>("FAILURE", message, data, timestamp);
    }
}


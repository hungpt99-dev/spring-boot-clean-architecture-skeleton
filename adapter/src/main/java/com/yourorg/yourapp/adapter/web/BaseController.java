package com.yourorg.yourapp.adapter.web;

import com.yourorg.yourapp.adapter.web.dto.ApiResponse;
import java.net.URI;
import java.time.Instant;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Shared helpers for web controllers to keep response construction consistent.
 */
public abstract class BaseController {

    protected Locale locale() {
        return LocaleContextHolder.getLocale();
    }

    protected <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        return ResponseEntity.ok(ApiResponse.success(message, data, Instant.now()));
    }

    protected <T> ResponseEntity<ApiResponse<T>> created(URI location, T data, String message) {
        return ResponseEntity.created(location)
            .body(ApiResponse.success(message, data, Instant.now()));
    }

    protected <T> ResponseEntity<ApiResponse<T>> conflict(String message) {
        return new ResponseEntity<>(ApiResponse.failure(message, null, Instant.now()), HttpStatus.CONFLICT);
    }

    protected <T> ResponseEntity<ApiResponse<T>> badRequest(String message) {
        return new ResponseEntity<>(ApiResponse.failure(message, null, Instant.now()), HttpStatus.BAD_REQUEST);
    }
}


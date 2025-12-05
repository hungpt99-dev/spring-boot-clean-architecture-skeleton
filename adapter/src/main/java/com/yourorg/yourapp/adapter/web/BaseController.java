package com.yourorg.yourapp.adapter.web;

import com.yourorg.yourapp.adapter.web.dto.ApiResponse;
import java.net.URI;
import java.time.Instant;
import java.util.Locale;
import java.util.function.Function;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Shared helpers for web controllers to keep response construction consistent.
 */
public abstract class BaseController {

    protected Locale locale() {
        return LocaleContextHolder.getLocale();
    }

    protected <D> ResponseEntity<ApiResponse<D>> ok(D data, String message) {
        return respond(HttpStatus.OK, message, data);
    }

    protected <D> ResponseEntity<ApiResponse<D>> created(URI location, D data, String message) {
        return ResponseEntity.created(location).body(success(message, data));
    }

    protected <D> ResponseEntity<ApiResponse<D>> conflict(String message) {
        return respond(HttpStatus.CONFLICT, message, null);
    }

    protected <D> ResponseEntity<ApiResponse<D>> badRequest(String message) {
        return respond(HttpStatus.BAD_REQUEST, message, null);
    }

    protected <D> ResponseEntity<ApiResponse<D>> respond(HttpStatus status, String message, D data) {
        return ResponseEntity.status(status).body(status.is2xxSuccessful() ? success(message, data) : failure(message, data));
    }

    protected <T, D> ResponseEntity<ApiResponse<D>> respond(HttpStatus status,
                                                           String message,
                                                           T source,
                                                           Function<T, D> mapper) {
        D mapped = source == null ? null : mapper.apply(source);
        return respond(status, message, mapped);
    }

    protected <D> ApiResponse<D> success(String message, D data) {
        return ApiResponse.success(message, data, Instant.now());
    }

    protected <D> ApiResponse<D> failure(String message, D data) {
        return ApiResponse.failure(message, data, Instant.now());
    }
}


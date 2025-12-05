package com.yourorg.yourapp.adapter.web;

import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        var details = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(GlobalExceptionHandler::formatFieldError)
            .collect(Collectors.toList());
        var response = ApiErrorResponse.of("Validation failed", details);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private static String formatFieldError(FieldError fieldError) {
        return "%s %s".formatted(fieldError.getField(), fieldError.getDefaultMessage());
    }
}


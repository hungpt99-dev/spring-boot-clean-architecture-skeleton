package com.yourorg.yourapp.adapter.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Simple request logging filter that enriches MDC with request metadata and logs completion.
 * Keeps logging concerns out of controllers/use-cases.
 */
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String REQUEST_ID = "requestId";
    private static final String CLIENT_IP = "clientIp";
    private static final String METHOD = "method";
    private static final String PATH = "path";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestId = resolveRequestId(request);
        long start = System.currentTimeMillis();
        try {
            MDC.put(REQUEST_ID, requestId);
            MDC.put(CLIENT_IP, clientIp(request));
            MDC.put(METHOD, request.getMethod());
            MDC.put(PATH, request.getRequestURI());
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            LOGGER.info("request completed status={} durationMs={}", response.getStatus(), duration);
            MDC.clear();
        }
    }

    private String resolveRequestId(HttpServletRequest request) {
        String existing = request.getHeader("X-Request-Id");
        if (existing != null && !existing.isBlank()) {
            return existing;
        }
        return UUID.randomUUID().toString();
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}


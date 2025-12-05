package com.yourorg.yourapp.adapter.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * Resolves requester region from headers; default header is X-Region.
 */
@Component
public class RegionResolver {

    private static final String REGION_HEADER = "X-Region";

    public String resolve(HttpServletRequest request) {
        if (request == null) {
            return "UNKNOWN";
        }
        String region = request.getHeader(REGION_HEADER);
        return (region == null || region.isBlank()) ? "UNKNOWN" : region.trim().toUpperCase();
    }
}


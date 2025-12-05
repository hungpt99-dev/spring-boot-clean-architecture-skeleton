package com.yourorg.yourapp.adapter.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * Simple CAPTCHA verifier that checks a header token. Replace with real verification.
 */
@Component
public class CaptchaVerifier {

    private static final String HEADER = "X-Captcha-Token";
    private static final String EXPECTED = "captcha-pass";

    public void verify(HttpServletRequest request) {
        if (request == null) {
            throw new IllegalStateException("Missing request for CAPTCHA verification");
        }
        String token = request.getHeader(HEADER);
        if (!EXPECTED.equals(token)) {
            throw new IllegalStateException("Invalid CAPTCHA");
        }
    }
}


package com.yourorg.yourapp.adapter.aspect;

import com.yourorg.yourapp.adapter.annotation.RateLimit;
import com.yourorg.yourapp.adapter.exception.RateLimitExceededException;
import com.yourorg.yourapp.adapter.service.RateLimiterService;
import java.lang.annotation.Annotation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * Scaffold for rate limiting. Integrate with your rate limiter (e.g., Redis/bucket4j).
 */
@Aspect
@Component
public class RateLimitAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimitAspect.class);
    private final RateLimiterService rateLimiterService;

    public RateLimitAspect(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Around("@within(com.yourorg.yourapp.adapter.annotation.RateLimit) || @annotation(com.yourorg.yourapp.adapter.annotation.RateLimit)")
    public Object enforce(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        RateLimit ann = sig.getMethod().getAnnotation(RateLimit.class);
        if (ann == null) {
            ann = AnnotatedElementUtils.findMergedAnnotation(sig.getDeclaringType(), RateLimit.class);
        } else {
            // use merged in case of composed annotations
            ann = AnnotatedElementUtils.findMergedAnnotation(sig.getMethod(), RateLimit.class);
        }
        if (ann != null && !rateLimiterService.allow(ann.value())) {
            LOGGER.warn("Rate limit exceeded for bucket {}", ann.value());
            throw new RateLimitExceededException(ann.value());
        }
        return pjp.proceed();
    }
}


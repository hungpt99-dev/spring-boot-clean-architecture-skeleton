package com.yourorg.yourapp.adapter.aspect;

import com.yourorg.yourapp.adapter.annotation.Idempotent;
import com.yourorg.yourapp.adapter.exception.DuplicateRequestException;
import com.yourorg.yourapp.adapter.service.IdempotencyService;
import com.yourorg.yourapp.adapter.support.HttpRequestUtils;
import jakarta.servlet.http.HttpServletRequest;
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
 * Scaffold for idempotency handling. Plug in your store (e.g., Redis) to track processed keys.
 */
@Aspect
@Component
public class IdempotentAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdempotentAspect.class);
    private final IdempotencyService idempotencyService;

    public IdempotentAspect(IdempotencyService idempotencyService) {
        this.idempotencyService = idempotencyService;
    }

    @Around("@within(com.yourorg.yourapp.adapter.annotation.Idempotent) || @annotation(com.yourorg.yourapp.adapter.annotation.Idempotent)")
    public Object enforce(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        Idempotent ann = sig.getMethod().getAnnotation(Idempotent.class);
        if (ann == null) {
            ann = AnnotatedElementUtils.findMergedAnnotation(sig.getDeclaringType(), Idempotent.class);
        } else {
            ann = AnnotatedElementUtils.findMergedAnnotation(sig.getMethod(), Idempotent.class);
        }
        if (ann != null) {
            HttpServletRequest request = HttpRequestUtils.currentRequest();
            String key = request != null ? request.getHeader(ann.key()) : null;
            if (key == null || key.isBlank()) {
                throw new DuplicateRequestException("Missing idempotency key: " + ann.key());
            }
            if (!idempotencyService.checkAndRecord(key)) {
                LOGGER.warn("Duplicate idempotent request key={}", key);
                throw new DuplicateRequestException(key);
            }
        }
        return pjp.proceed();
    }
}


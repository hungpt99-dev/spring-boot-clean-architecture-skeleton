package com.yourorg.yourapp.adapter.aspect;

import com.yourorg.yourapp.adapter.annotation.DeprecatedApi;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * Warns when deprecated APIs are invoked.
 */
@Aspect
@Component
public class DeprecatedApiAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeprecatedApiAspect.class);

    @Before("@within(com.yourorg.yourapp.adapter.annotation.DeprecatedApi) || @annotation(com.yourorg.yourapp.adapter.annotation.DeprecatedApi)")
    public void warn(JoinPoint joinPoint) {
        MethodSignature sig = (MethodSignature) joinPoint.getSignature();
        DeprecatedApi ann = sig.getMethod().getAnnotation(DeprecatedApi.class);
        if (ann == null) {
            ann = AnnotatedElementUtils.findMergedAnnotation(sig.getDeclaringType(), DeprecatedApi.class);
        } else {
            ann = AnnotatedElementUtils.findMergedAnnotation(sig.getMethod(), DeprecatedApi.class);
        }
        if (ann != null) {
            LOGGER.warn("Deprecated API called: {}.{} message='{}' sunset='{}'",
                sig.getDeclaringType().getSimpleName(),
                sig.getMethod().getName(),
                ann.message(),
                ann.sunsetVersion());
        }
    }
}


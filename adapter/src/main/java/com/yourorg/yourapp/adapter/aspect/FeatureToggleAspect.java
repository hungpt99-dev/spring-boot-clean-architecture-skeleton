package com.yourorg.yourapp.adapter.aspect;

import com.yourorg.yourapp.adapter.annotation.FeatureToggle;
import com.yourorg.yourapp.adapter.exception.FeatureDisabledException;
import com.yourorg.yourapp.adapter.service.FeatureFlagService;
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
 * Scaffold for feature toggle enforcement.
 * Plug in your toggle provider inside "isEnabled".
 */
@Aspect
@Component
public class FeatureToggleAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureToggleAspect.class);
    private final FeatureFlagService featureFlagService;

    public FeatureToggleAspect(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    @Around("@within(com.yourorg.yourapp.adapter.annotation.FeatureToggle) || @annotation(com.yourorg.yourapp.adapter.annotation.FeatureToggle)")
    public Object checkToggle(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        FeatureToggle ann = sig.getMethod().getAnnotation(FeatureToggle.class);
        if (ann == null) {
            ann = AnnotatedElementUtils.findMergedAnnotation(sig.getDeclaringType(), FeatureToggle.class);
        } else {
            ann = AnnotatedElementUtils.findMergedAnnotation(sig.getMethod(), FeatureToggle.class);
        }
        if (ann != null && !featureFlagService.isEnabled(ann.value())) {
            LOGGER.warn("Feature toggle '{}' is disabled; blocking execution of {}", ann.value(), sig.getMethod().getName());
            throw new FeatureDisabledException(ann.value());
        }
        return pjp.proceed();
    }
}


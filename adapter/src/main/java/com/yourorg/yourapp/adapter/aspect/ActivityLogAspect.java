package com.yourorg.yourapp.adapter.aspect;

import com.yourorg.yourapp.adapter.annotation.ActivityLog;
import com.yourorg.yourapp.adapter.service.ActivityLogger;
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
 * Scaffold for activity/audit logging.
 */
@Aspect
@Component
public class ActivityLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityLogAspect.class);
    private final ActivityLogger activityLogger;

    public ActivityLogAspect(ActivityLogger activityLogger) {
        this.activityLogger = activityLogger;
    }

    @Around("@within(com.yourorg.yourapp.adapter.annotation.ActivityLog) || @annotation(com.yourorg.yourapp.adapter.annotation.ActivityLog)")
    public Object logActivity(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        ActivityLog ann = sig.getMethod().getAnnotation(ActivityLog.class);
        if (ann == null) {
            ann = AnnotatedElementUtils.findMergedAnnotation(sig.getDeclaringType(), ActivityLog.class);
        } else {
            ann = AnnotatedElementUtils.findMergedAnnotation(sig.getMethod(), ActivityLog.class);
        }
        String action = ann != null ? ann.value() : sig.getMethod().getName();
        activityLogger.logStart(action);
        try {
            Object result = pjp.proceed();
            activityLogger.logSuccess(action);
            return result;
        } finally {
            // success already logged; failure logged in catch below
        }
        // failures are handled in the catch below
    }

    @Around("@within(com.yourorg.yourapp.adapter.annotation.ActivityLog) || @annotation(com.yourorg.yourapp.adapter.annotation.ActivityLog)")
    public Object logWithFailure(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return logActivity(pjp);
        } catch (Throwable t) {
            MethodSignature sig = (MethodSignature) pjp.getSignature();
            ActivityLog ann = AnnotatedElementUtils.findMergedAnnotation(sig.getMethod(), ActivityLog.class);
            if (ann == null) {
                ann = AnnotatedElementUtils.findMergedAnnotation(sig.getDeclaringType(), ActivityLog.class);
            }
            String action = ann != null ? ann.value() : sig.getMethod().getName();
            activityLogger.logFailure(action, t.getMessage());
            throw t;
        }
    }
}


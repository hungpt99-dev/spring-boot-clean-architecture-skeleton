package com.yourorg.yourapp.adapter.aspect;

import com.yourorg.yourapp.adapter.annotation.CaptchaRequired;
import com.yourorg.yourapp.adapter.exception.CaptchaFailedException;
import com.yourorg.yourapp.adapter.service.CaptchaVerifier;
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
 * Scaffold for CAPTCHA enforcement. Integrate with your CAPTCHA verification service.
 */
@Aspect
@Component
public class CaptchaAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaAspect.class);
    private final CaptchaVerifier captchaVerifier;

    public CaptchaAspect(CaptchaVerifier captchaVerifier) {
        this.captchaVerifier = captchaVerifier;
    }

    @Around("@within(com.yourorg.yourapp.adapter.annotation.CaptchaRequired) || @annotation(com.yourorg.yourapp.adapter.annotation.CaptchaRequired)")
    public Object verify(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        CaptchaRequired ann = findAnnotation(sig, CaptchaRequired.class);
        if (ann != null) {
            HttpServletRequest request = HttpRequestUtils.currentRequest();
            try {
                captchaVerifier.verify(request);
                LOGGER.debug("Captcha verified for {}", sig.getMethod().getName());
            } catch (IllegalStateException ex) {
                throw new CaptchaFailedException(ex.getMessage());
            }
        }
        return pjp.proceed();
    }

    private <A extends Annotation> A findAnnotation(MethodSignature sig, Class<A> type) {
        A ann = AnnotatedElementUtils.findMergedAnnotation(sig.getMethod(), type);
        if (ann == null) {
            ann = AnnotatedElementUtils.findMergedAnnotation(sig.getDeclaringType(), type);
        }
        return ann;
    }
}


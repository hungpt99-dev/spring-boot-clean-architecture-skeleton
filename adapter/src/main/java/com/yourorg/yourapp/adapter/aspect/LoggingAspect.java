package com.yourorg.yourapp.adapter.aspect;

import com.yourorg.yourapp.adapter.annotation.IgnoreLog;
import com.yourorg.yourapp.adapter.annotation.RequestLog;
import com.yourorg.yourapp.adapter.annotation.ResponseLog;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Simple logging aspect honoring @IgnoreLog, @RequestLog, @ResponseLog markers.
 * This is a lightweight scaffold; extend with masking logic and MDC enrichment as needed.
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("@within(com.yourorg.yourapp.adapter.annotation.IgnoreLog) || @annotation(com.yourorg.yourapp.adapter.annotation.IgnoreLog)")
    public Object skipLogging(ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
    }

    @Around("@within(com.yourorg.yourapp.adapter.annotation.RequestLog) || @annotation(com.yourorg.yourapp.adapter.annotation.RequestLog)")
    public Object logRequestResponse(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        RequestLog reqAnn = sig.getMethod().getAnnotation(RequestLog.class);
        ResponseLog resAnn = sig.getMethod().getAnnotation(ResponseLog.class);

        String method = sig.getDeclaringType().getSimpleName() + "." + sig.getMethod().getName();
        String argsSummary = summarizeArgs(pjp.getArgs(), reqAnn);

        LOGGER.info("REQ {} args={}", method, argsSummary);
        Object result = pjp.proceed();
        String resSummary = summarizeResult(result, resAnn);
        LOGGER.info("RES {} result={}", method, resSummary);
        return result;
    }

    private String summarizeArgs(Object[] args, RequestLog ann) {
        if (ann == null || ann.fields().length == 0) {
            return Arrays.stream(args)
                .map(arg -> arg == null ? "null" : arg.getClass().getSimpleName())
                .collect(Collectors.joining(","));
        }
        return "fields=" + String.join(",", ann.fields());
    }

    private String summarizeResult(Object result, ResponseLog ann) {
        if (ann == null || ann.fields().length == 0) {
            return result == null ? "null" : result.getClass().getSimpleName();
        }
        return "fields=" + String.join(",", ann.fields());
    }
}


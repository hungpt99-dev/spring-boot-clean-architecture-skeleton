package com.yourorg.yourapp.adapter.aspect;

import com.yourorg.yourapp.adapter.annotation.RegionRestricted;
import com.yourorg.yourapp.adapter.annotation.TimeRestricted;
import com.yourorg.yourapp.adapter.exception.RegionNotAllowedException;
import com.yourorg.yourapp.adapter.exception.TimeWindowViolationException;
import com.yourorg.yourapp.adapter.service.RegionResolver;
import com.yourorg.yourapp.adapter.support.HttpRequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.time.LocalTime;
import java.time.ZoneId;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * Scaffold for time/region restriction enforcement.
 */
@Aspect
@Component
public class TimeRegionRestrictionAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeRegionRestrictionAspect.class);
    private final RegionResolver regionResolver;

    public TimeRegionRestrictionAspect(RegionResolver regionResolver) {
        this.regionResolver = regionResolver;
    }

    @Around("@within(com.yourorg.yourapp.adapter.annotation.TimeRestricted) || @annotation(com.yourorg.yourapp.adapter.annotation.TimeRestricted)"
        + " || @within(com.yourorg.yourapp.adapter.annotation.RegionRestricted) || @annotation(com.yourorg.yourapp.adapter.annotation.RegionRestricted)")
    public Object enforce(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        TimeRestricted timeAnn = AnnotatedElementUtils.findMergedAnnotation(sig.getMethod(), TimeRestricted.class);
        if (timeAnn == null) {
            timeAnn = AnnotatedElementUtils.findMergedAnnotation(sig.getDeclaringType(), TimeRestricted.class);
        }
        if (timeAnn != null && isOutsideWindow(timeAnn)) {
            LOGGER.warn("Time restriction blocked execution of {}", sig.getMethod().getName());
            throw new TimeWindowViolationException(timeAnn.start(), timeAnn.end(), timeAnn.zone());
        }

        RegionRestricted regionAnn = AnnotatedElementUtils.findMergedAnnotation(sig.getMethod(), RegionRestricted.class);
        if (regionAnn == null) {
            regionAnn = AnnotatedElementUtils.findMergedAnnotation(sig.getDeclaringType(), RegionRestricted.class);
        }
        if (regionAnn != null && !isAllowedRegion(regionAnn)) {
            LOGGER.warn("Region restriction blocked execution of {}", sig.getMethod().getName());
            String region = regionResolver.resolve(HttpRequestUtils.currentRequest());
            throw new RegionNotAllowedException(region);
        }

        return pjp.proceed();
    }

    private boolean isOutsideWindow(TimeRestricted ann) {
        try {
            LocalTime start = LocalTime.parse(ann.start());
            LocalTime end = LocalTime.parse(ann.end());
            LocalTime now = LocalTime.now(ZoneId.of(ann.zone()));
            // inclusive start, exclusive end
            return now.isBefore(start) || !now.isBefore(end);
        } catch (Exception e) {
            LOGGER.warn("Time restriction parse error: {}", e.getMessage());
            return false;
        }
    }

    private boolean isAllowedRegion(RegionRestricted ann) {
        HttpServletRequest request = HttpRequestUtils.currentRequest();
        String region = regionResolver.resolve(request);
        for (String allowed : ann.allowed()) {
            if (allowed != null && allowed.equalsIgnoreCase(region)) {
                return true;
            }
        }
        return false;
    }
}


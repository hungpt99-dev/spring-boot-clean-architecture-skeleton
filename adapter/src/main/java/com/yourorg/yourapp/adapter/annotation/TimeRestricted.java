package com.yourorg.yourapp.adapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Restricts execution to a time window (24h format, inclusive start, exclusive end).
 * Example: @TimeRestricted(start = "08:00", end = "18:00", zone = "UTC")
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeRestricted {
    String start(); // "HH:mm"
    String end();   // "HH:mm"
    String zone() default "UTC";
}


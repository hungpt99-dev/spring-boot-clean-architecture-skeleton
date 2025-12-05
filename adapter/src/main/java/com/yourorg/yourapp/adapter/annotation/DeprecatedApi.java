package com.yourorg.yourapp.adapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an API/controller method as deprecated for clients.
 * Use message/version to signal migration paths.
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DeprecatedApi {
    String message() default "This API is deprecated";
    String sunsetVersion() default "";
}


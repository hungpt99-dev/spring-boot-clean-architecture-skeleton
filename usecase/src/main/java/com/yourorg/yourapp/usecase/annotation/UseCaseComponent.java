package com.yourorg.yourapp.usecase.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker for use case interactors. No Spring dependency.
 * Application layer can scan for this annotation and register beans automatically.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UseCaseComponent {
}


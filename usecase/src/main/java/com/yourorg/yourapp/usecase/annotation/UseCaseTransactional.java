package com.yourorg.yourapp.usecase.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a use case (interactor) as requiring a transactional boundary.
 * Pure marker without framework dependency; transaction is applied by an aspect in the application layer.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface UseCaseTransactional {
    boolean readOnly() default false;
}


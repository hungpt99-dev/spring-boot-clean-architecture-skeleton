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
    /**
     * When true, forces a new transaction (REQUIRES_NEW).
     */
    boolean requiresNew() default false;
    /**
     * Optional transaction timeout in seconds (ignored if < 0).
     */
    int timeoutSeconds() default -1;
    /**
     * Rollback on these exception types.
     */
    Class<? extends Throwable>[] rollbackFor() default {};
    /**
     * No rollback on these exception types.
     */
    Class<? extends Throwable>[] noRollbackFor() default {};
}


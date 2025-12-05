package com.yourorg.yourapp.adapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

/**
 * Marks classes as adapter-layer components.
 * Meta-annotated with {@link Component} to reduce boilerplate.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface AdapterComponent {
}


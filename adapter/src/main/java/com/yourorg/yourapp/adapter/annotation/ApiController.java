package com.yourorg.yourapp.adapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Meta-annotation combining @RestController, @Validated, and @RequestMapping.
 * Use value to set the base path.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RestController
@Validated
@RequestMapping
public @interface ApiController {
    String value() default "";
}
 

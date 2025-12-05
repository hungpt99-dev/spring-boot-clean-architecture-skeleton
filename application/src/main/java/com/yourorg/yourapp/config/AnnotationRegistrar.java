package com.yourorg.yourapp.config;

import com.yourorg.yourapp.domain.annotation.DomainComponent;
import com.yourorg.yourapp.usecase.annotation.UseCaseComponent;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

/**
 * Generic registrar that auto-registers classes annotated with specified annotations.
 * Currently handles:
 *  - @DomainComponent (default base: com.yourorg.yourapp.domain)
 *  - @UseCaseComponent (default base: com.yourorg.yourapp.usecase.interactor)
 */
@Component
public class AnnotationRegistrar implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private Environment environment;
    private final Map<Class<? extends Annotation>, String> targets = Map.of(
            DomainComponent.class, "app.domain.base-packages",
            UseCaseComponent.class, "app.usecase.base-packages"
    );

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        targets.forEach((ann, prop) -> registerFor(registry, ann, prop));
    }

    private void registerFor(BeanDefinitionRegistry registry,
                             Class<? extends Annotation> annotationType,
                             String propertyKey) {
        ClassPathScanningCandidateComponentProvider scanner =
            new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(annotationType));

        for (String basePackage : basePackages(propertyKey)) {
            for (BeanDefinition candidate : scanner.findCandidateComponents(basePackage)) {
                AbstractBeanDefinition bd = BeanDefinitionBuilder
                    .genericBeanDefinition(Objects.requireNonNull(candidate.getBeanClassName()))
                    .getBeanDefinition();
                BeanDefinitionReaderUtils.registerWithGeneratedName(bd, registry);
            }
        }
    }

    private List<String> basePackages(String propertyKey) {
        String configured = environment.getProperty(propertyKey);
        if (configured == null || configured.isBlank()) {
            return List.of();
        }
        return Arrays.stream(configured.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
    }

    @Override
    public void postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // no-op
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}


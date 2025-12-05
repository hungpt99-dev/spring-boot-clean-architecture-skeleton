package com.yourorg.yourapp.config;

import com.yourorg.yourapp.usecase.annotation.UseCaseComponent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

/**
 * Auto-registers use case interactors annotated with @Interactor.
 * Keeps the usecase module Spring-free; wiring happens in the application layer.
 */
@Component
public class InteractorRegistrar implements BeanDefinitionRegistryPostProcessor {

    private static final String USECASE_INTERACTOR_PACKAGE = "com.yourorg.yourapp.usecase.interactor";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ClassPathScanningCandidateComponentProvider scanner =
            new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(UseCaseComponent.class));

        for (BeanDefinition bd : scanner.findCandidateComponents(USECASE_INTERACTOR_PACKAGE)) {
            String beanName = bd.getBeanClassName();
            registry.registerBeanDefinition(beanName, bd);
        }
    }

    @Override
    public void postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // no-op
    }
}


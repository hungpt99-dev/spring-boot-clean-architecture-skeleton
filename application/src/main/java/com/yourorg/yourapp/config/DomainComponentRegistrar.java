package com.yourorg.yourapp.config;

import com.yourorg.yourapp.domain.annotation.DomainComponent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

/**
 * Auto-registers domain components annotated with @DomainComponent.
 */
@Component
public class DomainComponentRegistrar implements BeanDefinitionRegistryPostProcessor {

    private static final String DOMAIN_PACKAGE = "com.yourorg.yourapp.domain";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ClassPathScanningCandidateComponentProvider scanner =
            new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(DomainComponent.class));

        for (BeanDefinition bd : scanner.findCandidateComponents(DOMAIN_PACKAGE)) {
            String beanName = bd.getBeanClassName();
            registry.registerBeanDefinition(beanName, bd);
        }
    }

    @Override
    public void postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // no-op
    }
}


package com.yourorg.yourapp.config;

import java.util.List;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class LocalizationConfig implements WebMvcConfigurer {

    private final AppProperties appProperties;

    public LocalizationConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean
    public LocaleResolver localeResolver() {
        var resolver = new AcceptHeaderLocaleResolver();
        Locale defaultLocale = Locale.forLanguageTag(appProperties.localization().defaultLocale());
        resolver.setDefaultLocale(defaultLocale);
        List<String> supported = appProperties.localization().supportedLocales();
        resolver.setSupportedLocales(
            supported.stream().map(Locale::forLanguageTag).toList()
        );
        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        var interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public MessageSource messageSource() {
        var messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setDefaultLocale(Locale.forLanguageTag(appProperties.localization().fallbackLocale()));
        return messageSource;
    }
}


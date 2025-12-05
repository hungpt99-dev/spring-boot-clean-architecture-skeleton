package com.yourorg.yourapp.config;

import com.yourorg.yourapp.domain.factory.DefaultUserFactory;
import com.yourorg.yourapp.domain.factory.UserFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    UserFactory userFactory() {
        return new DefaultUserFactory();
    }
}


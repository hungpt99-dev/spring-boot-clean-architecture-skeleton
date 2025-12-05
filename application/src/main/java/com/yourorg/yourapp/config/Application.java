package com.yourorg.yourapp.config;

import com.yourorg.yourapp.domain.event.DomainEventPublisher;
import com.yourorg.yourapp.domain.factory.DefaultUserFactory;
import com.yourorg.yourapp.domain.factory.UserFactory;
import com.yourorg.yourapp.usecase.inputboundary.UserInputBoundary;
import com.yourorg.yourapp.usecase.interactor.UserRegisterInteractor;
import com.yourorg.yourapp.usecase.outputboundary.UserDsGateway;
import com.yourorg.yourapp.usecase.outputboundary.UserPresenter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    UserFactory userFactory() {
        return new DefaultUserFactory();
    }

    @Bean
    UserInputBoundary userRegisterInteractor(UserFactory userFactory,
                                             UserDsGateway userDsGateway,
                                             UserPresenter userPresenter,
                                             DomainEventPublisher domainEventPublisher) {
        return new UserRegisterInteractor(userFactory, userDsGateway, userPresenter, domainEventPublisher);
    }
}


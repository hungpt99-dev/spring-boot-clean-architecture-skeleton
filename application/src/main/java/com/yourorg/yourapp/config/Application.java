package com.yourorg.yourapp.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = { RabbitAutoConfiguration.class })
@EnableConfigurationProperties(AppProperties.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

package com.yourorg.yourapp.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.yourorg.yourapp.adapter.config.StorageProperties;

@SpringBootApplication(exclude = { RabbitAutoConfiguration.class })
@EnableConfigurationProperties({ AppProperties.class, StorageProperties.class })
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

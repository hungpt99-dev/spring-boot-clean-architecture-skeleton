package com.yourorg.yourapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "yourapp")
public record AppProperties(Environment environment,
                            Datasource datasource,
                            Messaging messaging,
                            Localization localization) {

    public record Environment(String name, String region) {}

    public record Datasource(String host, int port, String db, String username) {}

    public record Messaging(String broker, String exchange) {}

    public record Localization(String defaultLocale,
                               String fallbackLocale,
                               String defaultTimeZone,
                               java.util.List<String> supportedLocales) {}
}


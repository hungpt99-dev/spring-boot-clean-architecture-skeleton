package com.yourorg.yourapp.config;

import jakarta.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.TimeZone;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeZoneConfig {

    private final AppProperties appProperties;

    public TimeZoneConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @PostConstruct
    public void init() {
        String zone = appProperties.localization().defaultTimeZone();
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(zone)));
    }
}


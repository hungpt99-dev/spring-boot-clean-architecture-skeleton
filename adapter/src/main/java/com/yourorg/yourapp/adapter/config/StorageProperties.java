package com.yourorg.yourapp.adapter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "yourapp.storage")
public record StorageProperties(
    String endpoint,
    String region,
    String accessKey,
    String secretKey,
    String bucket,
    boolean pathStyleAccess
) {
    public StorageProperties {
        if (bucket == null || bucket.isBlank()) {
            throw new IllegalArgumentException("yourapp.storage.bucket must be provided");
        }
    }
}


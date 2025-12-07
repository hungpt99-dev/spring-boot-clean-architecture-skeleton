package com.yourorg.yourapp.adapter.config;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.utils.StringUtils;

@Configuration
public class ObjectStorageConfig {

    @Bean
    public S3Configuration s3Configuration(StorageProperties properties) {
        return S3Configuration.builder()
            .pathStyleAccessEnabled(properties.pathStyleAccess())
            .build();
    }

    @Bean
    public S3Client s3Client(StorageProperties properties, S3Configuration s3Configuration) {
        var creds = StaticCredentialsProvider.create(
            AwsBasicCredentials.create(properties.accessKey(), properties.secretKey()));

        var builder = S3Client.builder()
            .credentialsProvider(creds)
            .serviceConfiguration(s3Configuration)
            .region(resolveRegion(properties));

        if (StringUtils.isNotBlank(properties.endpoint())) {
            builder.endpointOverride(URI.create(properties.endpoint()));
        }

        return builder.build();
    }

    @Bean
    public S3Presigner s3Presigner(StorageProperties properties, S3Configuration s3Configuration) {
        var creds = StaticCredentialsProvider.create(
            AwsBasicCredentials.create(properties.accessKey(), properties.secretKey()));

        var builder = S3Presigner.builder()
            .credentialsProvider(creds)
            .serviceConfiguration(s3Configuration)
            .region(resolveRegion(properties));

        if (StringUtils.isNotBlank(properties.endpoint())) {
            builder.endpointOverride(URI.create(properties.endpoint()));
        }

        return builder.build();
    }

    private Region resolveRegion(StorageProperties properties) {
        if (StringUtils.isBlank(properties.region())) {
            return Region.US_EAST_1;
        }
        return Region.of(properties.region());
    }
}


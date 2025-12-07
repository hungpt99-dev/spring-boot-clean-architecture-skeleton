package com.yourorg.yourapp.adapter.service.storage;

import java.io.InputStream;
import java.time.Duration;

import org.springframework.stereotype.Component;

import com.yourorg.yourapp.adapter.config.StorageProperties;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
public class S3ObjectStorageClient implements ObjectStorageClient {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final StorageProperties properties;

    public S3ObjectStorageClient(S3Client s3Client, S3Presigner s3Presigner, StorageProperties properties) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.properties = properties;
        ensureBucketExists();
    }

    @Override
    public void upload(String key, InputStream content, long contentLength, String contentType) {
        var request = PutObjectRequest.builder()
            .bucket(properties.bucket())
            .key(key)
            .contentType(contentType)
            .build();

        s3Client.putObject(request, RequestBody.fromInputStream(content, contentLength));
    }

    @Override
    public String presignGet(String key, Duration expiresIn) {
        var getObject = GetObjectRequest.builder()
            .bucket(properties.bucket())
            .key(key)
            .build();

        var presign = GetObjectPresignRequest.builder()
            .signatureDuration(expiresIn)
            .getObjectRequest(getObject)
            .build();

        return s3Presigner.presignGetObject(presign).url().toString();
    }

    @Override
    public String presignPut(String key, String contentType, Duration expiresIn) {
        var putObject = PutObjectRequest.builder()
            .bucket(properties.bucket())
            .key(key)
            .contentType(contentType)
            .build();

        var presign = PutObjectPresignRequest.builder()
            .signatureDuration(expiresIn)
            .putObjectRequest(putObject)
            .build();

        return s3Presigner.presignPutObject(presign).url().toString();
    }

    @Override
    public void delete(String key) {
        s3Client.deleteObject(b -> b.bucket(properties.bucket()).key(key));
    }

    private void ensureBucketExists() {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(properties.bucket()).build());
        } catch (NoSuchBucketException e) {
            createBucketIfMissing();
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                createBucketIfMissing();
            } else {
                throw e;
            }
        }
    }

    private void createBucketIfMissing() {
        CreateBucketRequest request = CreateBucketRequest.builder()
            .bucket(properties.bucket())
            .build();
        CreateBucketResponse response = s3Client.createBucket(request);
    }
}


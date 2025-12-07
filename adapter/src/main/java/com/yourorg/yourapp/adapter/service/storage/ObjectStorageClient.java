package com.yourorg.yourapp.adapter.service.storage;

import java.io.InputStream;
import java.time.Duration;

public interface ObjectStorageClient {

    void upload(String key, InputStream content, long contentLength, String contentType);

    String presignGet(String key, Duration expiresIn);

    /**
     * Generate a pre-signed URL for uploading an object directly (PUT).
     */
    String presignPut(String key, String contentType, Duration expiresIn);

    void delete(String key);
}


package com.yourorg.yourapp.adapter.service;

import java.time.Duration;
import java.util.Objects;
import org.springframework.stereotype.Component;

/**
 * Idempotency store using CacheService (backed by Redis via Spring Cache).
 */
@Component
public class IdempotencyService {

    private static final Duration DEFAULT_TTL = Duration.ofMinutes(10);
    private final CacheService cacheService;

    public IdempotencyService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    /**
     * @return true if the key is new and recorded, false if duplicate within TTL.
     */
    public boolean checkAndRecord(String key) {
        Objects.requireNonNull(key, "idempotency key");
        String cacheKey = idemKey(key);
        Boolean existing = cacheService.getOrCreate(cacheKey, () -> Boolean.TRUE, DEFAULT_TTL);
        if (Boolean.TRUE.equals(existing)) {
            // first time, already stored
            return true;
        }
        // if another thread already set it, treat as duplicate
        cacheService.evict(cacheKey);
        return false;
    }

    private String idemKey(String key) {
        return "idem:" + key;
    }
}


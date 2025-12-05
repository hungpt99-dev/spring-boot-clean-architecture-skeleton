package com.yourorg.yourapp.adapter.service;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * Cache facade over Spring Cache (e.g., backed by Redis).
 * TTL should be configured at the cache manager/Redis level.
 */
@Component
public class CacheService {

    private static final String DEFAULT_CACHE = "app-cache";

    private final CacheManager cacheManager;

    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public <T> T getOrCreate(String key, Supplier<T> supplier, Duration ttl) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(supplier, "supplier");
        Cache cache = cacheManager.getCache(DEFAULT_CACHE);
        if (cache == null) {
            throw new IllegalStateException("Cache not configured: " + DEFAULT_CACHE);
        }
        Cache.ValueWrapper wrapper = cache.get(key);
        if (wrapper != null) {
            @SuppressWarnings("unchecked")
            T value = (T) wrapper.get();
            return value;
        }
        T created = supplier.get();
        cache.put(key, created);
        // TTL is managed by cache configuration (e.g., RedisCacheManager), not per entry here.
        return created;
    }

    public void evict(String key) {
        Cache cache = cacheManager.getCache(DEFAULT_CACHE);
        if (cache != null) {
            cache.evict(key);
        }
    }
}


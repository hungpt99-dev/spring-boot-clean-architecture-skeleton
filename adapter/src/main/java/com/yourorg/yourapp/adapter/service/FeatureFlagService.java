package com.yourorg.yourapp.adapter.service;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * Feature flag service with cache-backed storage. Replace the in-memory map with DB/Redis as needed.
 */
@Component
public class FeatureFlagService {

    private static final Duration DEFAULT_TTL = Duration.ofMinutes(5);

    private final CacheService cacheService;
    private final Map<String, Boolean> inMemory = new ConcurrentHashMap<>();

    public FeatureFlagService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public boolean isEnabled(String name) {
        Objects.requireNonNull(name, "feature name");
        Boolean cached = cacheService.getOrCreate(flagKey(name), () -> inMemory.getOrDefault(name, true), DEFAULT_TTL);
        return cached != null ? cached : true;
    }

    public void setFlag(String name, boolean enabled) {
        Objects.requireNonNull(name, "feature name");
        inMemory.put(name, enabled);
        cacheService.evict(flagKey(name));
    }

    private String flagKey(String name) {
        return "ff:" + name;
    }
}


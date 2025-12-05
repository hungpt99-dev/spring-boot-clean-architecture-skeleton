package com.yourorg.yourapp.adapter.service;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

/**
 * Rate limiter leveraging CacheService (backed by Redis via Spring Cache).
 */
@Component
public class RateLimiterService {

    private static final int DEFAULT_CAPACITY = 100;
    private static final Duration DEFAULT_WINDOW = Duration.ofMinutes(1);

    private final CacheService cacheService;

    public RateLimiterService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public boolean allow(String key) {
        Objects.requireNonNull(key, "rate limit key");
        String tokensKey = "rl:" + key + ":tokens";
        String windowKey = "rl:" + key + ":window";

        // Reset window if expired
        Long windowStart = cacheService.getOrCreate(windowKey, () -> System.currentTimeMillis(), DEFAULT_WINDOW);
        long now = System.currentTimeMillis();
        if (now - windowStart >= DEFAULT_WINDOW.toMillis()) {
            cacheService.evict(tokensKey);
            cacheService.evict(windowKey);
            windowStart = cacheService.getOrCreate(windowKey, () -> now, DEFAULT_WINDOW);
        }

        AtomicInteger tokens = cacheService.getOrCreate(tokensKey, () -> new AtomicInteger(DEFAULT_CAPACITY), DEFAULT_WINDOW);
        int remaining = tokens.decrementAndGet();
        if (remaining < 0) {
            return false;
        }
        return true;
    }
}


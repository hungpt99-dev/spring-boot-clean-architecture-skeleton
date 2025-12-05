package com.yourorg.yourapp.adapter.service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Component;

/**
 * Lock manager backed by a cache service. Replace with Redis/DB locks for production scale.
 */
@Component
public class DistributedLockManager {

    private static final Duration LOCK_TTL = Duration.ofMinutes(10);

    private final CacheService cacheService;

    public DistributedLockManager(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public LockHandle acquire(String name, long timeoutMillis) throws InterruptedException {
        ReentrantLock lock = cacheService.getOrCreate(name, ReentrantLock::new, LOCK_TTL);
        boolean acquired = lock.tryLock(timeoutMillis, TimeUnit.MILLISECONDS);
        if (!acquired) {
            throw new IllegalStateException("Could not acquire lock " + name);
        }
        return new LockHandle(lock);
    }

    public static final class LockHandle implements AutoCloseable {
        private final ReentrantLock lock;

        private LockHandle(ReentrantLock lock) {
            this.lock = lock;
        }

        @Override
        public void close() {
            lock.unlock();
        }
    }
}


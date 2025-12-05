package com.yourorg.yourapp.adapter.aspect;

import com.yourorg.yourapp.adapter.annotation.DistributedLock;
import com.yourorg.yourapp.adapter.exception.LockAcquisitionException;
import com.yourorg.yourapp.adapter.service.DistributedLockManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Scaffold for distributed locking. Integrate with Redis/DB lock as needed.
 */
@Aspect
@Component
public class DistributedLockAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedLockAspect.class);
    private final DistributedLockManager lockManager;

    public DistributedLockAspect(DistributedLockManager lockManager) {
        this.lockManager = lockManager;
    }

    @Around("@within(com.yourorg.yourapp.adapter.annotation.DistributedLock) || @annotation(com.yourorg.yourapp.adapter.annotation.DistributedLock)")
    public Object lock(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        DistributedLock ann = sig.getMethod().getAnnotation(DistributedLock.class);
        if (ann == null) {
            Class<?> declaring = sig.getDeclaringType();
            ann = declaring.getAnnotation(DistributedLock.class);
        }
        String lockName = ann != null ? ann.value() : "default-lock";
        LOGGER.debug("Acquiring distributed lock {}", lockName);
        try (DistributedLockManager.LockHandle ignored = lockManager.acquire(lockName, 1000)) {
            return pjp.proceed();
        } catch (IllegalStateException e) {
            throw new LockAcquisitionException(lockName);
        }
    }
}


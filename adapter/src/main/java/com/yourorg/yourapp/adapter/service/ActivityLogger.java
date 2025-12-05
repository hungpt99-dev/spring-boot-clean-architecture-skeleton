package com.yourorg.yourapp.adapter.service;

import com.yourorg.yourapp.adapter.persistence.entity.ActivityLogEntity;
import com.yourorg.yourapp.adapter.persistence.repository.ActivityLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * Activity logging facade persisting to the database and logging to SLF4J.
 */
@Component
public class ActivityLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityLogger.class);
    private final ActivityLogRepository repository;

    public ActivityLogger(ActivityLogRepository repository) {
        this.repository = repository;
    }

    public void logStart(String action) {
        save(action, ActivityLogEntity.Status.STARTED, null);
    }

    public void logSuccess(String action) {
        save(action, ActivityLogEntity.Status.SUCCESS, null);
    }

    public void logFailure(String action, String message) {
        save(action, ActivityLogEntity.Status.FAILURE, message);
    }

    private void save(String action, ActivityLogEntity.Status status, String message) {
        String reqId = MDC.get("requestId");
        repository.save(new ActivityLogEntity(action, status, reqId, message));
        if (status == ActivityLogEntity.Status.FAILURE) {
            LOGGER.warn("Activity {} status={} message={}", action, status, message);
        } else {
            LOGGER.info("Activity {} status={}", action, status);
        }
    }
}


package com.yourorg.yourapp.adapter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Activity logging facade. Replace with a real audit sink as needed.
 */
@Component
public class ActivityLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityLogger.class);

    public void logStart(String action) {
        LOGGER.info("Activity start action={}", action);
    }

    public void logEnd(String action) {
        LOGGER.info("Activity end action={}", action);
    }
}


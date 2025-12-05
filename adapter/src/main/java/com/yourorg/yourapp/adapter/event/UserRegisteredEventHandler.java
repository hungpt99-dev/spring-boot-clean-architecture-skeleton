package com.yourorg.yourapp.adapter.event;

import com.yourorg.yourapp.domain.event.UserRegisteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegisteredEventHandler.class);

    @EventListener
    public void onUserRegistered(UserRegisteredEvent event) {
        LOGGER.info("UserRegisteredEvent consumed for userId={}, email={}", event.userId().value(), event.email());
        // TODO: push the event to the messaging infrastructure (AMQP, Kafka, etc.)
    }
}


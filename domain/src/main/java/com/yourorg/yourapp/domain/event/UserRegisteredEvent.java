package com.yourorg.yourapp.domain.event;

import com.yourorg.yourapp.domain.model.UserId;
import java.time.Instant;

public record UserRegisteredEvent(UserId userId, String email, Instant occurredOn)
    implements DomainEvent {

    public UserRegisteredEvent {
        occurredOn = occurredOn == null ? Instant.now() : occurredOn;
    }

    @Override
    public String type() {
        return "user.registered";
    }
}


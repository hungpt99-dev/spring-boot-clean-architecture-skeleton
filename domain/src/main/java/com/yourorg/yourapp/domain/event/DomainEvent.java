package com.yourorg.yourapp.domain.event;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredOn();

    String type();
}


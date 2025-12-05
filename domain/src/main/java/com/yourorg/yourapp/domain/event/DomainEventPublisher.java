package com.yourorg.yourapp.domain.event;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}


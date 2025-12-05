package com.yourorg.yourapp.usecase.interactor;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

/**
 * Base class for interactors to share simple utilities (time, validation).
 * Keeps use cases framework-free.
 */
public abstract class BaseInteractor {

    private final Clock clock;

    protected BaseInteractor() {
        this(Clock.systemUTC());
    }

    protected BaseInteractor(Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    protected Instant now() {
        return Instant.now(clock);
    }

    protected <T> T requireNonNull(T value, String fieldName) {
        return Objects.requireNonNull(value, fieldName + " must not be null");
    }
}


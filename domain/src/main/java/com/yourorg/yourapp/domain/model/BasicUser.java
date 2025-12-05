package com.yourorg.yourapp.domain.model;

import java.time.Instant;
import java.util.Objects;

public final class BasicUser implements User {

    private final UserId id;
    private final String email;
    private final String displayName;
    private final UserStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;

    public BasicUser(UserId id,
                     String email,
                     String displayName,
                     UserStatus status,
                     Instant createdAt,
                     Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.email = Objects.requireNonNull(email, "email");
        this.displayName = Objects.requireNonNull(displayName, "displayName");
        this.status = Objects.requireNonNullElse(status, UserStatus.PENDING_VERIFICATION);
        this.createdAt = Objects.requireNonNullElseGet(createdAt, Instant::now);
        this.updatedAt = Objects.requireNonNullElseGet(updatedAt, Instant::now);
    }

    @Override
    public UserId id() {
        return id;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String displayName() {
        return displayName;
    }

    @Override
    public UserStatus status() {
        return status;
    }

    @Override
    public Instant createdAt() {
        return createdAt;
    }

    @Override
    public Instant updatedAt() {
        return updatedAt;
    }

    public BasicUser activate() {
        return new BasicUser(id, email, displayName, UserStatus.ACTIVE, createdAt, Instant.now());
    }
}


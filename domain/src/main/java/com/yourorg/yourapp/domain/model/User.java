package com.yourorg.yourapp.domain.model;

import java.time.Instant;

public interface User {
    UserId id();

    String email();

    String displayName();

    UserStatus status();

    Instant createdAt();

    Instant updatedAt();
}


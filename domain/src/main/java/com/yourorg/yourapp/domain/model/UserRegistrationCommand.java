package com.yourorg.yourapp.domain.model;

import java.util.Objects;

public record UserRegistrationCommand(String email, String displayName, char[] password) {

    public UserRegistrationCommand {
        Objects.requireNonNull(email, "email");
        Objects.requireNonNull(displayName, "displayName");
        Objects.requireNonNull(password, "password");
    }
}


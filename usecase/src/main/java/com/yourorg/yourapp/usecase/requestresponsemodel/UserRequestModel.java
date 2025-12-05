package com.yourorg.yourapp.usecase.requestresponsemodel;

import com.yourorg.yourapp.domain.model.UserRegistrationCommand;

public record UserRequestModel(String email, String displayName, String password) {

    public UserRegistrationCommand toCommand() {
        return new UserRegistrationCommand(
            email == null ? null : email.trim(),
            displayName == null ? null : displayName.trim(),
            password == null ? new char[0] : password.toCharArray()
        );
    }
}


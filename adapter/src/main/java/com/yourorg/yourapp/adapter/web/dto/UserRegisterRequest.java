package com.yourorg.yourapp.adapter.web.dto;

import com.yourorg.yourapp.usecase.requestresponsemodel.UserRequestModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(
    @Email @NotBlank String email,
    @NotBlank @Size(min = 2, max = 120) String displayName,
    @NotBlank @Size(min = 8, max = 72) String password
) {

    public UserRequestModel toRequestModel() {
        return new UserRequestModel(email, displayName, password);
    }
}


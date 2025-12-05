package com.yourorg.yourapp.adapter.web;

import com.yourorg.yourapp.adapter.web.dto.UserRegisterRequest;
import com.yourorg.yourapp.usecase.inputboundary.UserInputBoundary;
import com.yourorg.yourapp.usecase.requestresponsemodel.UserResponseModel;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.yourorg.yourapp.adapter.web.WebRoutes.API_V1_USERS;

@Validated
@RestController
@RequestMapping(API_V1_USERS)
public class UserRegisterController {

    private final UserInputBoundary userInputBoundary;

    public UserRegisterController(UserInputBoundary userInputBoundary) {
        this.userInputBoundary = userInputBoundary;
    }

    @PostMapping
    public ResponseEntity<UserResponseModel> register(@Valid @RequestBody UserRegisterRequest request) {
        UserResponseModel responseModel = userInputBoundary.registerUser(request.toRequestModel());
        HttpStatus status = "ALREADY_EXISTS".equals(responseModel.status())
            ? HttpStatus.CONFLICT
            : HttpStatus.CREATED;
        return new ResponseEntity<>(responseModel, status);
    }
}


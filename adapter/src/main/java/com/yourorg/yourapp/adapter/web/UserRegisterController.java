package com.yourorg.yourapp.adapter.web;

import com.yourorg.yourapp.adapter.annotation.ApiController;
import com.yourorg.yourapp.adapter.web.dto.UserRegisterRequest;
import com.yourorg.yourapp.usecase.requestresponsemodel.UserResponseModel;
import com.yourorg.yourapp.usecase.inputboundary.UserInputBoundary;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.yourorg.yourapp.adapter.web.WebRoutes.API_V1_USERS;

@ApiController(API_V1_USERS)
@RequiredArgsConstructor
public class UserRegisterController extends BaseController {

    private final UserInputBoundary userInputBoundary;

    @PostMapping
    public ResponseEntity<com.yourorg.yourapp.adapter.web.dto.ApiResponse<UserResponseModel>> register(@Valid @RequestBody UserRegisterRequest request) {
        UserResponseModel responseModel = userInputBoundary.registerUser(request.toRequestModel());
        boolean exists = "ALREADY_EXISTS".equals(responseModel.status());
        return exists
            ? conflict(responseModel.message())
            : created(null, responseModel, responseModel.message());
    }
}


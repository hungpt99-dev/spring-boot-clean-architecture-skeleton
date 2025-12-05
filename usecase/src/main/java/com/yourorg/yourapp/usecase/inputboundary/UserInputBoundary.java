package com.yourorg.yourapp.usecase.inputboundary;

import com.yourorg.yourapp.usecase.requestresponsemodel.UserRequestModel;
import com.yourorg.yourapp.usecase.requestresponsemodel.UserResponseModel;

public interface UserInputBoundary {
    UserResponseModel registerUser(UserRequestModel requestModel);
}


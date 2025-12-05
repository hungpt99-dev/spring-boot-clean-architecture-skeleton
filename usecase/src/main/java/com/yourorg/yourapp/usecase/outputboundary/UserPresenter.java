package com.yourorg.yourapp.usecase.outputboundary;

import com.yourorg.yourapp.domain.model.User;
import com.yourorg.yourapp.usecase.requestresponsemodel.UserResponseModel;

public interface UserPresenter {
    UserResponseModel present(User user);

    UserResponseModel presentAlreadyExists(String email);
}


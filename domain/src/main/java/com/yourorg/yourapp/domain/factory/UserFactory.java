package com.yourorg.yourapp.domain.factory;

import com.yourorg.yourapp.domain.model.BasicUser;
import com.yourorg.yourapp.domain.model.User;
import com.yourorg.yourapp.domain.model.UserRegistrationCommand;

public interface UserFactory {
    User create(UserRegistrationCommand command);

    default User createPending(UserRegistrationCommand command) {
        var user = create(command);
        if (user instanceof BasicUser basicUser) {
            return basicUser;
        }
        return user;
    }
}


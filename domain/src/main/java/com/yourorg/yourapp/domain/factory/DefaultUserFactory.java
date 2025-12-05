package com.yourorg.yourapp.domain.factory;

import com.yourorg.yourapp.domain.model.BasicUser;
import com.yourorg.yourapp.domain.model.User;
import com.yourorg.yourapp.domain.model.UserId;
import com.yourorg.yourapp.domain.model.UserRegistrationCommand;
import com.yourorg.yourapp.domain.model.UserStatus;
import java.time.Instant;

public final class DefaultUserFactory implements UserFactory {

    @Override
    public User create(UserRegistrationCommand command) {
        return new BasicUser(
            UserId.newId(),
            command.email().toLowerCase(),
            command.displayName(),
            UserStatus.PENDING_VERIFICATION,
            Instant.now(),
            Instant.now()
        );
    }
}


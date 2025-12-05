package com.yourorg.yourapp.usecase.outputboundary;

import com.yourorg.yourapp.domain.model.User;
import java.util.Optional;

public interface UserDsGateway {
    boolean existsByEmail(String email);

    User save(User user);

    Optional<User> findByEmail(String email);
}


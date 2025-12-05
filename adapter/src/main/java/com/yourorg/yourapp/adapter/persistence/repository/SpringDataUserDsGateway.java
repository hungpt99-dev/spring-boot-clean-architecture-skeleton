package com.yourorg.yourapp.adapter.persistence.repository;

import com.yourorg.yourapp.adapter.persistence.entity.UserDataMapper;
import com.yourorg.yourapp.adapter.persistence.entity.UserEntity;
import com.yourorg.yourapp.domain.model.User;
import com.yourorg.yourapp.usecase.outputboundary.UserDsGateway;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SpringDataUserDsGateway implements UserDsGateway {

    private final JpaUserRepository repository;

    public SpringDataUserDsGateway(JpaUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmailIgnoreCase(email);
    }

    @Override
    @Transactional
    public User save(User user) {
        UserEntity saved = repository.save(UserDataMapper.toEntity(user));
        return UserDataMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmailIgnoreCase(email).map(UserDataMapper::toDomain);
    }
}


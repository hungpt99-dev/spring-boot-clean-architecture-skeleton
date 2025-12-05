package com.yourorg.yourapp.adapter.persistence.repository;

import com.yourorg.yourapp.adapter.persistence.entity.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<UserEntity> findByEmailIgnoreCase(String email);
}


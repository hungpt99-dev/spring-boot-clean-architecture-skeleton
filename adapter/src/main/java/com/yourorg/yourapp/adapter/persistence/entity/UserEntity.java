package com.yourorg.yourapp.adapter.persistence.entity;

import com.yourorg.yourapp.domain.model.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity extends BaseAuditableEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 120)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UserStatus status;

    protected UserEntity() {
        // for JPA
    }

    public UserEntity(UUID id,
                      String email,
                      String displayName,
                      UserStatus status,
                      Instant createdAt,
                      Instant updatedAt) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UserStatus getStatus() {
        return status;
    }
}


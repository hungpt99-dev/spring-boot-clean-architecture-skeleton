package com.yourorg.yourapp.adapter.persistence.entity;

import com.yourorg.yourapp.domain.model.BasicUser;
import com.yourorg.yourapp.domain.model.User;
import com.yourorg.yourapp.domain.model.UserId;
import java.util.Objects;

public final class UserDataMapper {

    private UserDataMapper() {
    }

    public static UserEntity toEntity(User user) {
        Objects.requireNonNull(user, "user");
        return new UserEntity(
            user.id().value(),
            user.email(),
            user.displayName(),
            user.status(),
            user.createdAt(),
            user.updatedAt()
        );
    }

    public static User toDomain(UserEntity entity) {
        Objects.requireNonNull(entity, "entity");
        return new BasicUser(
            new UserId(entity.getId()),
            entity.getEmail(),
            entity.getDisplayName(),
            entity.getStatus(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}


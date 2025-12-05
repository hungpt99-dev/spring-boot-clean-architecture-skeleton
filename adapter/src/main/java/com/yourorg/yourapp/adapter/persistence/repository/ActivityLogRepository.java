package com.yourorg.yourapp.adapter.persistence.repository;

import com.yourorg.yourapp.adapter.persistence.entity.ActivityLogEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLogEntity, UUID> {
}


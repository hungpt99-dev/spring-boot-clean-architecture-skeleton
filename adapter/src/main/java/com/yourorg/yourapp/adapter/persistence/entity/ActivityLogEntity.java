package com.yourorg.yourapp.adapter.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "activity_logs")
public class ActivityLogEntity {

    public enum Status {
        STARTED, SUCCESS, FAILURE
    }

    @Id
    private UUID id;

    @Column(nullable = false, length = 200)
    private String action;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @Column(name = "request_id", length = 100)
    private String requestId;

    @Column(name = "message", length = 500)
    private String message;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public ActivityLogEntity() {}

    public ActivityLogEntity(String action, Status status, String requestId, String message) {
        this.action = action;
        this.status = status;
        this.requestId = requestId;
        this.message = message;
    }

    public UUID getId() {
        return id;
    }

    public String getAction() {
        return action;
    }

    public Status getStatus() {
        return status;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getMessage() {
        return message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}


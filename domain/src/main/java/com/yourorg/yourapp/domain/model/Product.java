package com.yourorg.yourapp.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public final class Product {

    private final ProductId id;
    private final String name;
    private final String sku;
    private final BigDecimal price;
    private final ProductStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Product(ProductId id,
                   String name,
                   String sku,
                   BigDecimal price,
                   ProductStatus status,
                   Instant createdAt,
                   Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.sku = Objects.requireNonNull(sku, "sku");
        this.price = Objects.requireNonNull(price, "price");
        this.status = Objects.requireNonNullElse(status, ProductStatus.ACTIVE);
        this.createdAt = Objects.requireNonNullElseGet(createdAt, Instant::now);
        this.updatedAt = Objects.requireNonNullElseGet(updatedAt, Instant::now);
    }

    public ProductId id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String sku() {
        return sku;
    }

    public BigDecimal price() {
        return price;
    }

    public ProductStatus status() {
        return status;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}


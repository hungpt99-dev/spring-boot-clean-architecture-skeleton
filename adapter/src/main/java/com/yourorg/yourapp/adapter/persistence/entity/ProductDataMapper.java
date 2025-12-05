package com.yourorg.yourapp.adapter.persistence.entity;

import com.yourorg.yourapp.domain.model.Product;
import com.yourorg.yourapp.domain.model.ProductId;
import java.util.Objects;

public final class ProductDataMapper {

    private ProductDataMapper() {
    }

    public static ProductEntity toEntity(Product product) {
        Objects.requireNonNull(product, "product");
        return new ProductEntity(
            product.id().value(),
            product.name(),
            product.sku(),
            product.price(),
            product.status()
        );
    }

    public static Product toDomain(ProductEntity entity) {
        Objects.requireNonNull(entity, "entity");
        return new Product(
            new ProductId(entity.getId()),
            entity.getName(),
            entity.getSku(),
            entity.getPrice(),
            entity.getStatus(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}


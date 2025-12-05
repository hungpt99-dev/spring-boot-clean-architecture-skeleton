package com.yourorg.yourapp.domain.factory;

import com.yourorg.yourapp.domain.annotation.DomainComponent;
import com.yourorg.yourapp.domain.model.Product;
import com.yourorg.yourapp.domain.model.ProductId;
import com.yourorg.yourapp.domain.model.ProductStatus;
import java.math.BigDecimal;
import java.time.Instant;

@DomainComponent
public final class DefaultProductFactory implements ProductFactory {

    @Override
    public Product create(String name, String sku, BigDecimal price) {
        return new Product(
            ProductId.newId(),
            name,
            sku.toUpperCase(),
            price,
            ProductStatus.ACTIVE,
            Instant.now(),
            Instant.now()
        );
    }
}


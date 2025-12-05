package com.yourorg.yourapp.domain.factory;

import com.yourorg.yourapp.domain.model.Product;
import com.yourorg.yourapp.domain.model.ProductId;
import com.yourorg.yourapp.domain.model.ProductStatus;
import java.math.BigDecimal;
import java.time.Instant;

public interface ProductFactory {
    Product create(String name, String sku, BigDecimal price);
}


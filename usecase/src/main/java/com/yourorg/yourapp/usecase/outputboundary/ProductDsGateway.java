package com.yourorg.yourapp.usecase.outputboundary;

import com.yourorg.yourapp.domain.model.Product;
import java.util.Optional;

public interface ProductDsGateway {
    boolean existsBySku(String sku);

    Product save(Product product);

    Optional<Product> findBySku(String sku);
}


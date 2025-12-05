package com.yourorg.yourapp.adapter.web;

import com.yourorg.yourapp.domain.model.Product;
import com.yourorg.yourapp.usecase.outputboundary.ProductPresenter;
import com.yourorg.yourapp.usecase.requestresponsemodel.ProductResponseModel;
import org.springframework.stereotype.Component;

@Component
public class ProductPresenterImpl implements ProductPresenter {

    @Override
    public ProductResponseModel present(Product product) {
        return new ProductResponseModel(
            product.id().value().toString(),
            product.name(),
            product.sku(),
            product.status().name(),
            "Product created"
        );
    }

    @Override
    public ProductResponseModel presentAlreadyExists(String sku) {
        return new ProductResponseModel(null, null, sku, "ALREADY_EXISTS", "Product already exists");
    }
}


package com.yourorg.yourapp.usecase.outputboundary;

import com.yourorg.yourapp.domain.model.Product;
import com.yourorg.yourapp.usecase.requestresponsemodel.ProductResponseModel;

public interface ProductPresenter {
    ProductResponseModel present(Product product);

    ProductResponseModel presentAlreadyExists(String sku);
}


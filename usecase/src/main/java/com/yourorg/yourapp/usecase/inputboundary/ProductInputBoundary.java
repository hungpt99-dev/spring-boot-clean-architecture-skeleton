package com.yourorg.yourapp.usecase.inputboundary;

import com.yourorg.yourapp.usecase.requestresponsemodel.ProductRequestModel;
import com.yourorg.yourapp.usecase.requestresponsemodel.ProductResponseModel;

public interface ProductInputBoundary {
    ProductResponseModel create(ProductRequestModel request);
}


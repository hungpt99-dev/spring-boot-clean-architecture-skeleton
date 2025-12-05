package com.yourorg.yourapp.adapter.web;

import static com.yourorg.yourapp.adapter.web.WebRoutes.API_V1_PRODUCTS;

import com.yourorg.yourapp.adapter.annotation.ApiController;
import com.yourorg.yourapp.adapter.web.dto.ApiResponse;
import com.yourorg.yourapp.adapter.web.dto.ProductCreateRequest;
import com.yourorg.yourapp.usecase.inputboundary.ProductInputBoundary;
import com.yourorg.yourapp.usecase.requestresponsemodel.ProductResponseModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@ApiController(API_V1_PRODUCTS)
@RequiredArgsConstructor
public class ProductController extends BaseController {

    private final ProductInputBoundary productInputBoundary;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseModel>> create(@Valid @RequestBody ProductCreateRequest request) {
        ProductResponseModel responseModel = productInputBoundary.create(request.toRequestModel());
        boolean exists = "ALREADY_EXISTS".equals(responseModel.status());
        return exists
            ? conflict(responseModel.message())
            : created(null, responseModel, responseModel.message());
    }
}


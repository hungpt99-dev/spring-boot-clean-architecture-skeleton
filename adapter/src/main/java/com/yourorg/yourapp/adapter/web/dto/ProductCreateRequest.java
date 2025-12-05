package com.yourorg.yourapp.adapter.web.dto;

import com.yourorg.yourapp.usecase.requestresponsemodel.ProductRequestModel;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductCreateRequest(
    @NotBlank @Size(max = 200) String name,
    @NotBlank @Size(max = 100) String sku,
    @NotNull @DecimalMin("0.01") BigDecimal price
) {
    public ProductRequestModel toRequestModel() {
        return new ProductRequestModel(name, sku, price);
    }
}


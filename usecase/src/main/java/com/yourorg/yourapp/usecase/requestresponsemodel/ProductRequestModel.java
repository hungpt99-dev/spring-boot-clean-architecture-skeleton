package com.yourorg.yourapp.usecase.requestresponsemodel;

import java.math.BigDecimal;

public record ProductRequestModel(String name, String sku, BigDecimal price) {
}


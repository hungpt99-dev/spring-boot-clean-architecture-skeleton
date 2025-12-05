package com.yourorg.yourapp.usecase.interactor;

import com.yourorg.yourapp.domain.factory.ProductFactory;
import com.yourorg.yourapp.domain.model.Product;
import com.yourorg.yourapp.usecase.annotation.UseCaseComponent;
import com.yourorg.yourapp.usecase.annotation.UseCaseTransactional;
import com.yourorg.yourapp.usecase.inputboundary.ProductInputBoundary;
import com.yourorg.yourapp.usecase.outputboundary.ProductDsGateway;
import com.yourorg.yourapp.usecase.outputboundary.ProductPresenter;
import com.yourorg.yourapp.usecase.requestresponsemodel.ProductRequestModel;
import com.yourorg.yourapp.usecase.requestresponsemodel.ProductResponseModel;

@UseCaseComponent
@UseCaseTransactional
public class ProductCreateInteractor implements ProductInputBoundary {

    private final ProductFactory productFactory;
    private final ProductDsGateway productGateway;
    private final ProductPresenter presenter;

    public ProductCreateInteractor(ProductFactory productFactory,
                                   ProductDsGateway productGateway,
                                   ProductPresenter presenter) {
        this.productFactory = productFactory;
        this.productGateway = productGateway;
        this.presenter = presenter;
    }

    @Override
    public ProductResponseModel create(ProductRequestModel request) {
        if (productGateway.existsBySku(request.sku())) {
            return presenter.presentAlreadyExists(request.sku());
        }
        Product product = productFactory.create(request.name(), request.sku(), request.price());
        Product saved = productGateway.save(product);
        return presenter.present(saved);
    }
}


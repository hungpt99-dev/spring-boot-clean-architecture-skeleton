package com.yourorg.yourapp.usecase.interactor;

import com.yourorg.yourapp.domain.event.DomainEventPublisher;
import com.yourorg.yourapp.domain.event.UserRegisteredEvent;
import com.yourorg.yourapp.domain.factory.UserFactory;
import com.yourorg.yourapp.domain.model.User;
import com.yourorg.yourapp.usecase.inputboundary.UserInputBoundary;
import com.yourorg.yourapp.usecase.outputboundary.UserDsGateway;
import com.yourorg.yourapp.usecase.outputboundary.UserPresenter;
import com.yourorg.yourapp.usecase.requestresponsemodel.UserRequestModel;
import com.yourorg.yourapp.usecase.requestresponsemodel.UserResponseModel;
import java.time.Instant;

public class UserRegisterInteractor implements UserInputBoundary {

    private final UserFactory userFactory;
    private final UserDsGateway userGateway;
    private final UserPresenter presenter;
    private final DomainEventPublisher eventPublisher;

    public UserRegisterInteractor(UserFactory userFactory,
                                  UserDsGateway userGateway,
                                  UserPresenter presenter,
                                  DomainEventPublisher eventPublisher) {
        this.userFactory = userFactory;
        this.userGateway = userGateway;
        this.presenter = presenter;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UserResponseModel registerUser(UserRequestModel requestModel) {
        if (userGateway.existsByEmail(requestModel.email())) {
            return presenter.presentAlreadyExists(requestModel.email());
        }

        User user = userFactory.create(requestModel.toCommand());
        User persisted = userGateway.save(user);

        eventPublisher.publish(new UserRegisteredEvent(persisted.id(), persisted.email(), Instant.now()));

        return presenter.present(persisted);
    }
}


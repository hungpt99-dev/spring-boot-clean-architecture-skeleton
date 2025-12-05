package com.yourorg.yourapp.adapter.web;

import com.yourorg.yourapp.domain.model.User;
import com.yourorg.yourapp.usecase.outputboundary.UserPresenter;
import com.yourorg.yourapp.usecase.requestresponsemodel.UserResponseModel;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserPresenterImpl implements UserPresenter {

    private final MessageSource messageSource;

    public UserPresenterImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public UserResponseModel present(User user) {
        Locale locale = LocaleContextHolder.getLocale();
        return new UserResponseModel(
            user.id().value().toString(),
            user.email(),
            user.status().name(),
            messageSource.getMessage("user.register.success", null, locale)
        );
    }

    @Override
    public UserResponseModel presentAlreadyExists(String email) {
        Locale locale = LocaleContextHolder.getLocale();
        return new UserResponseModel(
            null,
            email,
            "ALREADY_EXISTS",
            messageSource.getMessage("user.register.exists", new Object[] {email}, locale)
        );
    }
}


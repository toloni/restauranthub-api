package br.com.postechfiap.toloni.restauranthub.infrastructure.web.rest.usertype;

import br.com.postechfiap.toloni.restauranthub.adapters.presenters.usertype.UserTypeViewModel;
import br.com.postechfiap.toloni.restauranthub.domain.usertype.UserRole;

import java.util.UUID;

public record UserTypeResponse(UUID id, String name, String description, UserRole role) {

    public static UserTypeResponse from(UserTypeViewModel viewModel) {
        return new UserTypeResponse(viewModel.id(), viewModel.name(), viewModel.description(), viewModel.role());
    }
}

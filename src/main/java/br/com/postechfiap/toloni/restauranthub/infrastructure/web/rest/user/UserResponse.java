package br.com.postechfiap.toloni.restauranthub.infrastructure.web.rest.user;

import br.com.postechfiap.toloni.restauranthub.adapters.presenters.user.UserViewModel;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

/// Represents the HTTP response body for [User] operations.
///
/// @param id           the unique identifier of the user
/// @param name         the name of the user
/// @param email        the email of the user
/// @param userTypeId   the unique identifier of the user type
/// @param userTypeName the name of the user type
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(UUID id, String name, String email, UUID userTypeId, String userTypeName) {

    public static UserResponse from(UserViewModel viewModel) {
        return new UserResponse(
                viewModel.id(),
                viewModel.name(),
                viewModel.email(),
                viewModel.userTypeId(),
                viewModel.userTypeName()
        );
    }
}

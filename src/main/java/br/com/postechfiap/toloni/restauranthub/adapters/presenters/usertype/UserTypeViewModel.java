package br.com.postechfiap.toloni.restauranthub.adapters.presenters.usertype;

import br.com.postechfiap.toloni.restauranthub.domain.usertype.UserRole;

import java.util.UUID;

/// View model representing a [UserType] for the adapter layer.
///
/// @param id          the unique identifier of the user type
/// @param name        the name of the user type
/// @param description the description of the user type
/// @param role        the [UserRole] associated with this user type
public record UserTypeViewModel(
        UUID id,
        String name,
        String description,
        UserRole role
) {
}

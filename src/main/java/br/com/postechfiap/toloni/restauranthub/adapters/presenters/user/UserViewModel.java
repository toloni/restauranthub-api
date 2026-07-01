package br.com.postechfiap.toloni.restauranthub.adapters.presenters.user;

import java.util.UUID;

/// View model representing a [User] for the adapter layer.
///
/// Carries all displayable user data, including the optional user type name
/// populated only by read queries that join with the user type.
///
/// @param id           the unique identifier of the user
/// @param name         the name of the user
/// @param email        the email of the user
/// @param userTypeId   the unique identifier of the user type
/// @param userTypeName the name of the user type, or {@code null} on write operations
public record UserViewModel(
        UUID id,
        String name,
        String email,
        UUID userTypeId,
        String userTypeName
) {
}

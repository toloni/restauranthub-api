package br.com.postechfiap.toloni.restauranthub.adapters.presenters.restaurant;

import java.util.UUID;

/// View model representing a [Restaurant] for the adapter layer.
///
/// Carries all displayable restaurant data, including the optional owner name
/// populated only by read queries that join with the owner user.
///
/// @param id           the unique identifier of the restaurant
/// @param name         the name of the restaurant
/// @param address      the address of the restaurant
/// @param cuisineType  the cuisine type of the restaurant
/// @param openingHours the opening hours of the restaurant
/// @param ownerId      the unique identifier of the owner user
/// @param ownerName    the name of the owner user, or {@code null} on write operations
public record RestaurantViewModel(
        UUID id,
        String name,
        String address,
        String cuisineType,
        String openingHours,
        UUID ownerId,
        String ownerName
) {
}

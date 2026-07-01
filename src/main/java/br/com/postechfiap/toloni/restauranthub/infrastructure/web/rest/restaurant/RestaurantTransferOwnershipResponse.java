package br.com.postechfiap.toloni.restauranthub.infrastructure.web.rest.restaurant;

import br.com.postechfiap.toloni.restauranthub.adapters.presenters.restaurant.TransferOwnershipViewModel;

/// Represents the HTTP response body for [Restaurant] ownership transfer operations.
///
/// @param newOwnerId the unique identifier of the new owner
public record RestaurantTransferOwnershipResponse(String newOwnerId) {

    public static RestaurantTransferOwnershipResponse from(TransferOwnershipViewModel viewModel) {
        return new RestaurantTransferOwnershipResponse(viewModel.newOwnerId().toString());
    }
}

package br.com.postechfiap.toloni.restauranthub.adapters.presenters.restaurant;

import java.util.UUID;

/// View model representing the result of a [Restaurant] ownership transfer.
///
/// @param newOwnerId the unique identifier of the new restaurant owner
public record TransferOwnershipViewModel(UUID newOwnerId) {
}

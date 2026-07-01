package br.com.postechfiap.toloni.restauranthub.adapters.controllers;

import br.com.postechfiap.toloni.restauranthub.adapters.presenters.restaurant.RestaurantPresenter;
import br.com.postechfiap.toloni.restauranthub.adapters.presenters.restaurant.RestaurantViewModel;
import br.com.postechfiap.toloni.restauranthub.adapters.presenters.restaurant.TransferOwnershipViewModel;
import br.com.postechfiap.toloni.restauranthub.application.pagination.Page;
import br.com.postechfiap.toloni.restauranthub.application.usecases.restaurant.*;

/// Adapter that bridges any entry point to the [Restaurant] use cases.
///
/// Receives input from the presentation layer — regardless of protocol
/// (HTTP, gRPC, CLI, messaging) — delegates to the appropriate use case,
/// and returns the result.
public class RestaurantController {

    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final UpdateRestaurantUseCase updateRestaurantUseCase;
    private final DeleteRestaurantUseCase deleteRestaurantUseCase;
    private final FindRestaurantByIdUseCase findRestaurantByIdUseCase;
    private final FindAllRestaurantsUseCase findAllRestaurantsUseCase;
    private final TransferRestaurantOwnershipUseCase transferRestaurantOwnershipUseCase;
    private final RestaurantPresenter restaurantPresenter;

    /// @param createRestaurantUseCase            the use case for creating a [Restaurant]
    /// @param updateRestaurantUseCase            the use case for updating a [Restaurant]
    /// @param deleteRestaurantUseCase            the use case for deleting a [Restaurant]
    /// @param findRestaurantByIdUseCase          the use case for finding a [Restaurant] by its identifier
    /// @param findAllRestaurantsUseCase          the use case for retrieving all [Restaurant] instances
    /// @param transferRestaurantOwnershipUseCase the use case for transferring [Restaurant] ownership
    /// @param restaurantPresenter                the presenter that converts use case outputs to view models
    public RestaurantController(
            CreateRestaurantUseCase createRestaurantUseCase,
            UpdateRestaurantUseCase updateRestaurantUseCase,
            DeleteRestaurantUseCase deleteRestaurantUseCase,
            FindRestaurantByIdUseCase findRestaurantByIdUseCase,
            FindAllRestaurantsUseCase findAllRestaurantsUseCase,
            TransferRestaurantOwnershipUseCase transferRestaurantOwnershipUseCase,
            RestaurantPresenter restaurantPresenter) {
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.updateRestaurantUseCase = updateRestaurantUseCase;
        this.deleteRestaurantUseCase = deleteRestaurantUseCase;
        this.findRestaurantByIdUseCase = findRestaurantByIdUseCase;
        this.findAllRestaurantsUseCase = findAllRestaurantsUseCase;
        this.transferRestaurantOwnershipUseCase = transferRestaurantOwnershipUseCase;
        this.restaurantPresenter = restaurantPresenter;
    }

    /// Creates a new [Restaurant].
    ///
    /// @param input the [CreateRestaurantUseCase.Input] data
    /// @return the [CreateRestaurantUseCase.Output] containing the created [Restaurant] data
    /// @throws AlreadyExistsException if a [Restaurant] with the given name already exists
    /// @throws NotFoundException      if no [User] is found with the given [UserId]
    public RestaurantViewModel create(CreateRestaurantUseCase.Input input) {
        return restaurantPresenter.present(createRestaurantUseCase.execute(input));
    }

    /// Updates an existing [Restaurant].
    ///
    /// @param input the [UpdateRestaurantUseCase.Input] data
    /// @return the [UpdateRestaurantUseCase.Output] containing the updated [Restaurant] data
    /// @throws NotFoundException      if no [Restaurant] or [User] is found
    /// @throws AlreadyExistsException if another [Restaurant] with the given name already exists
    public RestaurantViewModel update(UpdateRestaurantUseCase.Input input) {
        return restaurantPresenter.present(updateRestaurantUseCase.execute(input));
    }

    /// Deletes a [Restaurant] by its identifier.
    ///
    /// This operation is idempotent — if no [Restaurant] is found, completes silently.
    ///
    /// @param input the [DeleteRestaurantUseCase.Input] data
    public void delete(DeleteRestaurantUseCase.Input input) {
        deleteRestaurantUseCase.execute(input);
    }

    /// Finds a [Restaurant] by its identifier.
    ///
    /// @param input the [FindRestaurantByIdUseCase.Input] data
    /// @return the [RestaurantViewModel] containing the found [Restaurant] data
    /// @throws NotFoundException if no [Restaurant] is found with the given [RestaurantId]
    public RestaurantViewModel findById(FindRestaurantByIdUseCase.Input input) {
        return restaurantPresenter.present(findRestaurantByIdUseCase.execute(input));
    }

    /// Retrieves a paginated list of [Restaurant] instances.
    ///
    /// @param input the [FindAllRestaurantsUseCase.Input] carrying the [PageRequest]
    /// @return a [Page] of [RestaurantViewModel]
    public Page<RestaurantViewModel> findAll(FindAllRestaurantsUseCase.Input input) {
        var output = findAllRestaurantsUseCase.execute(input);
        var content = output.content().stream()
                .map(restaurantPresenter::present)
                .toList();
        return Page.of(content, output.pageNumber(), output.pageSize(), output.totalElements());
    }

    /// Transfers the ownership of a [Restaurant] to a new owner.
    ///
    /// This operation is restricted to users with the [UserRole#ADMIN] role.
    /// The new owner must have the [UserRole#RESTAURANT_OWNER] role.
    ///
    /// @param input the [TransferRestaurantOwnershipUseCase.Input] data
    /// @return the [TransferRestaurantOwnershipUseCase.Output] containing the new owner identifier
    /// @throws UnauthorizedException if the requester is not an admin or the new owner is not a restaurant owner
    /// @throws NotFoundException     if no [Restaurant] or [User] is found
    public TransferOwnershipViewModel transferOwnership(
            TransferRestaurantOwnershipUseCase.Input input) {
        return restaurantPresenter.present(transferRestaurantOwnershipUseCase.execute(input));
    }
}

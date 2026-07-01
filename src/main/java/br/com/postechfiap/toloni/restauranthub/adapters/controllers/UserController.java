package br.com.postechfiap.toloni.restauranthub.adapters.controllers;

import br.com.postechfiap.toloni.restauranthub.adapters.presenters.user.UserPresenter;
import br.com.postechfiap.toloni.restauranthub.adapters.presenters.user.UserViewModel;
import br.com.postechfiap.toloni.restauranthub.application.pagination.Page;
import br.com.postechfiap.toloni.restauranthub.application.usecases.user.*;

/// Adapter that bridges any entry point to the [User] use cases.
///
/// Receives input from the presentation layer — regardless of protocol
/// (HTTP, gRPC, CLI, messaging) — delegates to the appropriate use case,
/// and returns the result.
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final FindAllUsersUseCase findAllUsersUseCase;
    private final UserPresenter userPresenter;

    /// @param createUserUseCase   the use case for creating a [User]
    /// @param updateUserUseCase   the use case for updating a [User]
    /// @param deleteUserUseCase   the use case for deleting a [User]
    /// @param findUserByIdUseCase the use case for finding a [User] by its identifier
    /// @param findAllUsersUseCase the use case for retrieving all [User] instances
    /// @param userPresenter       the presenter for mapping use case outputs to view models
    public UserController(
            CreateUserUseCase createUserUseCase,
            UpdateUserUseCase updateUserUseCase,
            DeleteUserUseCase deleteUserUseCase,
            FindUserByIdUseCase findUserByIdUseCase,
            FindAllUsersUseCase findAllUsersUseCase,
            UserPresenter userPresenter) {
        this.createUserUseCase = createUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.findUserByIdUseCase = findUserByIdUseCase;
        this.findAllUsersUseCase = findAllUsersUseCase;
        this.userPresenter = userPresenter;
    }

    /// Creates a new [User].
    ///
    /// @param input the [CreateUserUseCase.Input] data
    /// @return the [UserViewModel] containing the created [User] data
    /// @throws AlreadyExistsException if a [User] with the given email already exists
    public UserViewModel create(CreateUserUseCase.Input input) {
        return userPresenter.present(createUserUseCase.execute(input));
    }

    /// Updates an existing [User].
    ///
    /// @param input the [UpdateUserUseCase.Input] data
    /// @return the [UserViewModel] containing the updated [User] data
    /// @throws NotFoundException      if no [User] is found with the given [UserId]
    /// @throws AlreadyExistsException if another [User] with the given email already exists
    public UserViewModel update(UpdateUserUseCase.Input input) {
        return userPresenter.present(updateUserUseCase.execute(input));
    }

    /// Deletes a [User] by its identifier.
    ///
    /// This operation is idempotent — if no [User] is found, completes silently.
    ///
    /// @param input the [DeleteUserUseCase.Input] data
    public void delete(DeleteUserUseCase.Input input) {
        deleteUserUseCase.execute(input);
    }

    /// Finds a [User] by its identifier.
    ///
    /// @param input the [FindUserByIdUseCase.Input] data
    /// @return the [UserViewModel] containing the found [User] data
    /// @throws NotFoundException if no [User] is found with the given [UserId]
    public UserViewModel findById(FindUserByIdUseCase.Input input) {
        return userPresenter.present(findUserByIdUseCase.execute(input));
    }

    /// Retrieves a paginated list of [User] instances.
    ///
    /// @param input the [FindAllUsersUseCase.Input] carrying the [PageRequest]
    /// @return a [Page] of [UserViewModel]
    public Page<UserViewModel> findAll(FindAllUsersUseCase.Input input) {
        var output = findAllUsersUseCase.execute(input);
        var content = output.content().stream()
                .map(userPresenter::present)
                .toList();
        return Page.of(content, output.pageNumber(), output.pageSize(), output.totalElements());
    }
}

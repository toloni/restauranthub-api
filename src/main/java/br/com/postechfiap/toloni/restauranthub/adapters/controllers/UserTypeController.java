package br.com.postechfiap.toloni.restauranthub.adapters.controllers;

import br.com.postechfiap.toloni.restauranthub.adapters.presenters.usertype.UserTypePresenter;
import br.com.postechfiap.toloni.restauranthub.adapters.presenters.usertype.UserTypeViewModel;
import br.com.postechfiap.toloni.restauranthub.application.pagination.Page;
import br.com.postechfiap.toloni.restauranthub.application.usecases.usertype.*;

/// Adapter that bridges any entry point to the [UserType] use cases.
///
/// Receives input from the presentation layer — regardless of protocol
/// (HTTP, gRPC, CLI, messaging) — delegates to the appropriate use case,
/// and returns the result.
public class UserTypeController {

    private final CreateUserTypeUseCase createUserTypeUseCase;
    private final UpdateUserTypeUseCase updateUserTypeUseCase;
    private final DeleteUserTypeUseCase deleteUserTypeUseCase;
    private final FindUserTypeByIdUseCase findUserTypeByIdUseCase;
    private final FindAllUserTypesUseCase findAllUserTypesUseCase;
    private final UserTypePresenter userTypePresenter;

    /// @param createUserTypeUseCase   the use case for creating a [UserType]
    /// @param updateUserTypeUseCase   the use case for updating a [UserType]
    /// @param deleteUserTypeUseCase   the use case for deleting a [UserType]
    /// @param findUserTypeByIdUseCase the use case for finding a [UserType] by its identifier
    /// @param findAllUserTypesUseCase the use case for retrieving all [UserType] instances
    /// @param userTypePresenter       the presenter for mapping use case outputs to view models
    public UserTypeController(
            CreateUserTypeUseCase createUserTypeUseCase,
            UpdateUserTypeUseCase updateUserTypeUseCase,
            DeleteUserTypeUseCase deleteUserTypeUseCase,
            FindUserTypeByIdUseCase findUserTypeByIdUseCase,
            FindAllUserTypesUseCase findAllUserTypesUseCase,
            UserTypePresenter userTypePresenter) {
        this.createUserTypeUseCase = createUserTypeUseCase;
        this.updateUserTypeUseCase = updateUserTypeUseCase;
        this.deleteUserTypeUseCase = deleteUserTypeUseCase;
        this.findUserTypeByIdUseCase = findUserTypeByIdUseCase;
        this.findAllUserTypesUseCase = findAllUserTypesUseCase;
        this.userTypePresenter = userTypePresenter;
    }

    /// Creates a new [UserType].
    ///
    /// @param input the [CreateUserTypeUseCase.Input] data
    /// @return the [UserTypeViewModel] containing the created [UserType] data
    /// @throws AlreadyExistsException if a [UserType] with the given role already exists
    public UserTypeViewModel create(CreateUserTypeUseCase.Input input) {
        return userTypePresenter.present(createUserTypeUseCase.execute(input));
    }

    /// Updates an existing [UserType].
    ///
    /// @param input the [UpdateUserTypeUseCase.Input] data
    /// @return the [UserTypeViewModel] containing the updated [UserType] data
    /// @throws NotFoundException      if no [UserType] is found with the given [UserTypeId]
    /// @throws AlreadyExistsException if another [UserType] with the given role already exists
    public UserTypeViewModel update(UpdateUserTypeUseCase.Input input) {
        return userTypePresenter.present(updateUserTypeUseCase.execute(input));
    }

    /// Deletes a [UserType] by its identifier.
    ///
    /// This operation is idempotent — if no [UserType] is found, completes silently.
    ///
    /// @param input the [DeleteUserTypeUseCase.Input] data
    public void delete(DeleteUserTypeUseCase.Input input) {
        deleteUserTypeUseCase.execute(input);
    }

    /// Finds a [UserType] by its identifier.
    ///
    /// @param input the [FindUserTypeByIdUseCase.Input] data
    /// @return the [UserTypeViewModel] containing the found [UserType] data
    /// @throws NotFoundException if no [UserType] is found with the given [UserTypeId]
    public UserTypeViewModel findById(FindUserTypeByIdUseCase.Input input) {
        return userTypePresenter.present(findUserTypeByIdUseCase.execute(input));
    }

    /// Retrieves a paginated list of [UserType] instances.
    ///
    /// @param input the [FindAllUserTypesUseCase.Input] carrying the [PageRequest]
    /// @return a [Page] of [UserTypeViewModel]
    public Page<UserTypeViewModel> findAll(FindAllUserTypesUseCase.Input input) {
        var output = findAllUserTypesUseCase.execute(input);
        var content = output.content().stream()
                .map(userTypePresenter::present)
                .toList();
        return Page.of(content, output.pageNumber(), output.pageSize(), output.totalElements());
    }
}

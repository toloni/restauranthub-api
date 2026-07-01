package br.com.postechfiap.toloni.restauranthub.adapters.controllers;

import br.com.postechfiap.toloni.restauranthub.adapters.presenters.menuitem.MenuItemPresenter;
import br.com.postechfiap.toloni.restauranthub.adapters.presenters.menuitem.MenuItemViewModel;
import br.com.postechfiap.toloni.restauranthub.application.pagination.Page;
import br.com.postechfiap.toloni.restauranthub.application.usecases.menuitem.*;

/// Adapter that bridges any entry point to the [MenuItem] use cases.
///
/// Receives input from the presentation layer — regardless of protocol
/// (HTTP, gRPC, CLI, messaging) — delegates to the appropriate use case,
/// and returns the result.
public class MenuItemController {

    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final UpdateMenuItemUseCase updateMenuItemUseCase;
    private final DeleteMenuItemUseCase deleteMenuItemUseCase;
    private final FindMenuItemByIdUseCase findMenuItemByIdUseCase;
    private final FindAllMenuItemsUseCase findAllMenuItemsUseCase;
    private final MenuItemPresenter menuItemPresenter;

    /// @param createMenuItemUseCase   the use case for creating a [MenuItem]
    /// @param updateMenuItemUseCase   the use case for updating a [MenuItem]
    /// @param deleteMenuItemUseCase   the use case for deleting a [MenuItem]
    /// @param findMenuItemByIdUseCase the use case for finding a [MenuItem] by its identifier
    /// @param findAllMenuItemsUseCase the use case for retrieving all [MenuItem] instances
    /// @param menuItemPresenter       the presenter for mapping use case outputs to view models
    public MenuItemController(
            CreateMenuItemUseCase createMenuItemUseCase,
            UpdateMenuItemUseCase updateMenuItemUseCase,
            DeleteMenuItemUseCase deleteMenuItemUseCase,
            FindMenuItemByIdUseCase findMenuItemByIdUseCase,
            FindAllMenuItemsUseCase findAllMenuItemsUseCase,
            MenuItemPresenter menuItemPresenter) {
        this.createMenuItemUseCase = createMenuItemUseCase;
        this.updateMenuItemUseCase = updateMenuItemUseCase;
        this.deleteMenuItemUseCase = deleteMenuItemUseCase;
        this.findMenuItemByIdUseCase = findMenuItemByIdUseCase;
        this.findAllMenuItemsUseCase = findAllMenuItemsUseCase;
        this.menuItemPresenter = menuItemPresenter;
    }

    /// Creates a new [MenuItem].
    ///
    /// @param input the [CreateMenuItemUseCase.Input] data
    /// @return the [MenuItemViewModel] containing the created [MenuItem] data
    /// @throws NotFoundException      if no [Restaurant] is found with the given [RestaurantId]
    /// @throws UnauthorizedException  if the requester is not the owner of the [Restaurant]
    /// @throws AlreadyExistsException if a [MenuItem] with the given name already exists in the [Restaurant]
    public MenuItemViewModel create(CreateMenuItemUseCase.Input input) {
        return menuItemPresenter.present(createMenuItemUseCase.execute(input));
    }

    /// Updates an existing [MenuItem].
    ///
    /// @param input the [UpdateMenuItemUseCase.Input] data
    /// @return the [MenuItemViewModel] containing the updated [MenuItem] data
    /// @throws NotFoundException      if no [MenuItem] or [Restaurant] is found
    /// @throws UnauthorizedException  if the requester is not the owner of the [Restaurant]
    /// @throws AlreadyExistsException if another [MenuItem] with the given name already exists in the [Restaurant]
    public MenuItemViewModel update(UpdateMenuItemUseCase.Input input) {
        return menuItemPresenter.present(updateMenuItemUseCase.execute(input));
    }

    /// Deletes a [MenuItem] by its identifier.
    ///
    /// @param input the [DeleteMenuItemUseCase.Input] data
    /// @throws NotFoundException     if no [MenuItem] or [Restaurant] is found
    /// @throws UnauthorizedException if the requester is not the owner of the [Restaurant]
    public void delete(DeleteMenuItemUseCase.Input input) {
        deleteMenuItemUseCase.execute(input);
    }

    /// Finds a [MenuItem] by its identifier.
    ///
    /// @param input the [FindMenuItemByIdUseCase.Input] data
    /// @return the [MenuItemViewModel] containing the found [MenuItem] data
    /// @throws NotFoundException if no [MenuItem] is found with the given [MenuItemId]
    public MenuItemViewModel findById(FindMenuItemByIdUseCase.Input input) {
        return menuItemPresenter.present(findMenuItemByIdUseCase.execute(input));
    }

    /// Retrieves a paginated list of [MenuItem] instances.
    ///
    /// @param input the [FindAllMenuItemsUseCase.Input] carrying the [PageRequest]
    /// @return a [Page] of [MenuItemViewModel]
    public Page<MenuItemViewModel> findAll(FindAllMenuItemsUseCase.Input input) {
        var output = findAllMenuItemsUseCase.execute(input);
        var content = output.content().stream()
                .map(menuItemPresenter::present)
                .toList();
        return Page.of(content, output.pageNumber(), output.pageSize(), output.totalElements());
    }
}

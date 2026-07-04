package br.com.postechfiap.toloni.restauranthub.infrastructure.web.rest.menuitem;

import br.com.postechfiap.toloni.restauranthub.application.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/// API interface for [MenuItem] operations.
///
/// Defines the contract for the REST layer, separating Swagger
/// documentation from the controller implementation.
///
/// Creation and restaurant-scoped listing are exposed as a sub-resource of
/// `restaurants` (`/api/v1/restaurants/{restaurantId}/menu-items`), since a
/// [MenuItem] only exists within a [Restaurant]. Item-level operations and
/// the global listing remain flat under `/api/v1/menu-items`.
@Tag(name = "Menu Items", description = "Operations related to menu items")
public interface MenuItemApi {

    @Operation(summary = "Create a menu item", description = "Creates a new menu item for the given restaurant.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Menu item created successfully"),
            @ApiResponse(responseCode = "403", description = "Requester is not the owner of the restaurant"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "409", description = "Menu item with the given name already exists in the restaurant")
    })
    @PostMapping("/api/v1/restaurants/{restaurantId}/menu-items")
    @ResponseStatus(HttpStatus.CREATED)
    MenuItemResponse create(
            @Parameter(description = "Restaurant identifier", required = true) @PathVariable String restaurantId,
            @Parameter(description = "Authenticated user identifier", required = true) @RequestHeader("X-User-Id") String userId,
            @RequestBody MenuItemRequest request);

    @Operation(summary = "Update a menu item", description = "Updates an existing menu item by its identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu item updated successfully"),
            @ApiResponse(responseCode = "403", description = "Requester is not the owner of the restaurant"),
            @ApiResponse(responseCode = "404", description = "Menu item or restaurant not found"),
            @ApiResponse(responseCode = "409", description = "Menu item with the given name already exists in the restaurant")
    })
    @PatchMapping("/api/v1/menu-items/{id}")
    MenuItemResponse update(
            @Parameter(description = "Menu item identifier", required = true) @PathVariable String id,
            @Parameter(description = "Authenticated user identifier", required = true) @RequestHeader("X-User-Id") String userId,
            @RequestBody MenuItemRequest request);

    @Operation(summary = "Delete a menu item", description = "Deletes a menu item by its identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Menu item deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Requester is not the owner of the restaurant"),
            @ApiResponse(responseCode = "404", description = "Menu item or restaurant not found")
    })
    @DeleteMapping("/api/v1/menu-items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(
            @Parameter(description = "Menu item identifier", required = true) @PathVariable String id,
            @Parameter(description = "Authenticated user identifier", required = true) @RequestHeader("X-User-Id") String userId);

    @Operation(summary = "Find a menu item by id", description = "Retrieves a menu item by its identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu item found"),
            @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    @GetMapping("/api/v1/menu-items/{id}")
    MenuItemResponse findById(
            @Parameter(description = "Menu item identifier", required = true) @PathVariable String id);

    @Operation(summary = "Find menu items of a restaurant", description = "Retrieves a paginated list of menu items belonging to the given restaurant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu items retrieved successfully")
    })
    @GetMapping("/api/v1/restaurants/{restaurantId}/menu-items")
    Page<MenuItemResponse> findAllByRestaurant(
            @Parameter(description = "Restaurant identifier", required = true) @PathVariable String restaurantId,
            @Parameter(description = "Page number (zero-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by", example = "name") @RequestParam(required = false) String sort,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC") @RequestParam(required = false) String direction,
            @Parameter(description = "Field to filter by", example = "name") @RequestParam(required = false) String filter,
            @Parameter(description = "Filter value", example = "pizza") @RequestParam(required = false) String filterValue);

    @Operation(summary = "Find all menu items", description = "Retrieves a paginated list of menu items across all restaurants.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu items retrieved successfully")
    })
    @GetMapping("/api/v1/menu-items")
    Page<MenuItemResponse> findAll(
            @Parameter(description = "Page number (zero-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by", example = "name") @RequestParam(required = false) String sort,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC") @RequestParam(required = false) String direction,
            @Parameter(description = "Field to filter by", example = "name") @RequestParam(required = false) String filter,
            @Parameter(description = "Filter value", example = "pizza") @RequestParam(required = false) String filterValue);
}

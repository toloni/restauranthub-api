package br.com.postechfiap.toloni.restauranthub.infrastructure.web.rest.menuitem;

import br.com.postechfiap.toloni.restauranthub.adapters.presenters.menuitem.MenuItemViewModel;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.UUID;

/// Represents the HTTP response body for [MenuItem] operations.
///
/// @param id             the unique identifier of the menu item
/// @param name           the name of the menu item
/// @param description    the description of the menu item
/// @param price          the price of the menu item
/// @param currency       the currency code of the price
/// @param dineInOnly     whether this item is available for dine-in only
/// @param imagePath      the image path of the menu item
/// @param restaurantId   the unique identifier of the restaurant this item belongs to
/// @param restaurantName the name of the restaurant this item belongs to
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MenuItemResponse(UUID id, String name, String description, BigDecimal price,
                               String currency, boolean dineInOnly, String imagePath,
                               UUID restaurantId, String restaurantName) {

    public static MenuItemResponse from(MenuItemViewModel viewModel) {
        return new MenuItemResponse(
                viewModel.id(),
                viewModel.name(),
                viewModel.description(),
                viewModel.price(),
                viewModel.currency().getCurrencyCode(),
                viewModel.dineInOnly(),
                viewModel.imagePath(),
                viewModel.restaurantId(),
                viewModel.restaurantName()
        );
    }
}

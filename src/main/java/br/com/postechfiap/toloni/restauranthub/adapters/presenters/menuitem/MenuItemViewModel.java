package br.com.postechfiap.toloni.restauranthub.adapters.presenters.menuitem;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

/// View model representing a [MenuItem] for the adapter layer.
///
/// Carries all displayable menu item data, including the optional restaurant name
/// populated only by read queries that join with the restaurant.
///
/// @param id             the unique identifier of the menu item
/// @param name           the name of the menu item
/// @param description    the description of the menu item
/// @param price          the price amount of the menu item
/// @param currency       the currency of the price
/// @param dineInOnly     whether the item is available for dine-in only
/// @param imagePath      the path to the menu item image
/// @param restaurantId   the unique identifier of the restaurant this item belongs to
/// @param restaurantName the name of the restaurant, or {@code null} on write operations
public record MenuItemViewModel(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Currency currency,
        boolean dineInOnly,
        String imagePath,
        UUID restaurantId,
        String restaurantName
) {
}

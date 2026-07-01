package br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.repositories;

import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.entities.MenuItemJpaEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/// Spring Data JPA repository for [MenuItemJpaEntity].
///
/// Extends [JpaRepository] and [JpaSpecificationExecutor] to provide standard CRUD
/// operations, pagination, and dynamic specification-based filtering.
public interface MenuItemJpaRepository extends JpaRepository<MenuItemJpaEntity, UUID>,
        JpaSpecificationExecutor<MenuItemJpaEntity> {

    /// Checks whether a menu item with the given name exists in the given restaurant.
    ///
    /// @param name         the menu item name to check
    /// @param restaurantId the restaurant UUID to scope the check
    /// @return `true` if a menu item with that name exists in the restaurant
    boolean existsByNameAndRestaurantId(String name, UUID restaurantId);

    /// Checks whether a menu item with the given name exists in the given restaurant,
    /// excluding the one with the given id.
    ///
    /// @param name         the menu item name to check
    /// @param restaurantId the restaurant UUID to scope the check
    /// @param id           the menu item id to exclude
    /// @return `true` if another menu item with that name exists in the restaurant
    boolean existsByNameAndRestaurantIdAndIdNot(String name, UUID restaurantId, UUID id);

    /// Checks whether any menu item belongs to the given restaurant.
    ///
    /// @param restaurantId the restaurant UUID to check
    /// @return `true` if the restaurant has at least one menu item
    boolean existsByRestaurantId(UUID restaurantId);

    /// Returns a paginated list of menu items belonging to the given restaurant.
    ///
    /// @param restaurantId the restaurant UUID to filter by
    /// @param pageable     the pagination parameters
    /// @return a [Page] of [MenuItemJpaEntity]
    Page<MenuItemJpaEntity> findAllByRestaurantId(UUID restaurantId, Pageable pageable);

    /// Deletes all menu items belonging to the given restaurant.
    ///
    /// @param restaurantId the restaurant UUID whose items should be deleted
    @Transactional
    void deleteAllByRestaurantId(UUID restaurantId);
}

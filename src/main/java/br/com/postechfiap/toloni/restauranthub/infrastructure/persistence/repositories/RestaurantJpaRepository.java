package br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.repositories;

import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.entities.RestaurantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/// Spring Data JPA repository for [RestaurantJpaEntity].
///
/// Extends [JpaRepository] and [JpaSpecificationExecutor] to provide standard CRUD
/// operations, pagination, and dynamic specification-based filtering.
public interface RestaurantJpaRepository extends JpaRepository<RestaurantJpaEntity, UUID>,
        JpaSpecificationExecutor<RestaurantJpaEntity> {

    /// Checks whether a restaurant with the given name exists.
    ///
    /// @param name the restaurant name to check
    /// @return `true` if a restaurant with that name exists
    boolean existsByName(String name);

    /// Checks whether a restaurant with the given name exists, excluding the one with the given id.
    ///
    /// @param name the restaurant name to check
    /// @param id   the id to exclude from the search
    /// @return `true` if another restaurant with that name exists
    boolean existsByNameAndIdNot(String name, UUID id);

    /// Checks whether a restaurant owned by the given owner id exists.
    ///
    /// @param ownerId the owner's UUID to check
    /// @return `true` if a restaurant with that owner exists
    boolean existsByOwnerId(UUID ownerId);
}

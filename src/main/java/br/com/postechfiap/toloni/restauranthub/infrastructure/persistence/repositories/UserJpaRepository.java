package br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.repositories;

import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.entities.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/// Spring Data JPA repository for [UserJpaEntity].
///
/// Extends [JpaRepository] and [JpaSpecificationExecutor] to provide standard CRUD
/// operations, pagination, and dynamic specification-based filtering.
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID>,
        JpaSpecificationExecutor<UserJpaEntity> {

    /// Checks whether a user with the given email exists.
    ///
    /// @param email the email to check
    /// @return `true` if a user with that email exists
    boolean existsByEmail(String email);

    /// Checks whether a user with the given email exists, excluding the one with the given id.
    ///
    /// @param email the email to check
    /// @param id    the id to exclude from the search
    /// @return `true` if another user with that email exists
    boolean existsByEmailAndIdNot(String email, UUID id);

    /// Checks whether any user is associated with the given user type id.
    ///
    /// @param userTypeId the user type UUID to check
    /// @return `true` if at least one user has that user type
    boolean existsByUserTypeId(UUID userTypeId);
}

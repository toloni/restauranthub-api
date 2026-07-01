package br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.repositories;

import br.com.postechfiap.toloni.restauranthub.domain.usertype.UserRole;
import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.entities.UserTypeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/// Spring Data JPA repository for [UserTypeJpaEntity].
///
/// Extends [JpaRepository] and [JpaSpecificationExecutor] to provide standard CRUD
/// operations, pagination, and dynamic specification-based filtering.
public interface UserTypeJpaRepository extends JpaRepository<UserTypeJpaEntity, UUID>,
        JpaSpecificationExecutor<UserTypeJpaEntity> {

    /// Checks whether a user type with the given role exists.
    ///
    /// @param role the [UserRole] to check
    /// @return `true` if a user type with that role exists
    boolean existsByRole(UserRole role);

    /// Checks whether a user type with the given role exists, excluding the one with the given id.
    ///
    /// @param role the [UserRole] to check
    /// @param id   the id to exclude from the search
    /// @return `true` if another user type with that role exists
    boolean existsByRoleAndIdNot(UserRole role, UUID id);
}

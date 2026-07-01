package br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.entities;

import br.com.postechfiap.toloni.restauranthub.domain.usertype.UserRole;
import br.com.postechfiap.toloni.restauranthub.domain.usertype.UserType;
import br.com.postechfiap.toloni.restauranthub.domain.usertype.valueobject.UserTypeDescription;
import br.com.postechfiap.toloni.restauranthub.domain.usertype.valueobject.UserTypeId;
import br.com.postechfiap.toloni.restauranthub.domain.usertype.valueobject.UserTypeName;
import jakarta.persistence.*;

import java.util.UUID;

/// JPA entity representing a [UserType] in the persistence layer.
@Entity
@Table(name = "user_types")
public class UserTypeJpaEntity {

    @Id
    private UUID id;
    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    /// Creates a [UserTypeJpaEntity] from a [UserType] domain entity.
    ///
    /// @param userType the [UserType] domain entity to convert
    /// @return the corresponding [UserTypeJpaEntity]
    public static UserTypeJpaEntity fromDomain(UserType userType) {
        var entity = new UserTypeJpaEntity();
        entity.id = userType.getId().getValue();
        entity.name = userType.getName().getValue();
        entity.description = userType.getDescription().getValue();
        entity.role = userType.getRole();
        return entity;
    }

    /// Converts this JPA entity to a [UserType] domain entity.
    ///
    /// @return the corresponding [UserType]
    public UserType toDomain() {
        return new UserType(
                UserTypeId.of(id),
                UserTypeName.of(name),
                UserTypeDescription.of(description),
                role
        );
    }

    /// Sets the identifier of this entity (used when creating a reference by ID).
    ///
    /// @param value the UUID to set
    public void setId(UUID value) {
        this.id = value;
    }

    /// @return the unique identifier of this user type
    public UUID getId() {
        return id;
    }

    /// @return the name of this user type
    public String getName() {
        return name;
    }
}
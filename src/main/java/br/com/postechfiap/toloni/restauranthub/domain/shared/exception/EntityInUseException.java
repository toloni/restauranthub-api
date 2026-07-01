package br.com.postechfiap.toloni.restauranthub.domain.shared.exception;

/// Thrown when an attempt is made to delete a resource that is referenced by other entities.
///
/// Extends [DomainException] and maps to HTTP 409 Conflict at the presentation layer.
public class EntityInUseException extends DomainException {
    public EntityInUseException(String entity, String id) {
        super(entity + " is in use and cannot be deleted: " + id);
    }
}

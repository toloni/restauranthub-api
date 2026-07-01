package br.com.postechfiap.toloni.restauranthub.domain.shared.exception;

/// Thrown when an attempt is made to create a resource that already exists.
///
/// Extends [DomainException] and maps to HTTP 409 Conflict at the presentation layer.
public class AlreadyExistsException extends DomainException {
    public AlreadyExistsException(String field, String value) {
        super(field + " already exists: " + value);
    }
}

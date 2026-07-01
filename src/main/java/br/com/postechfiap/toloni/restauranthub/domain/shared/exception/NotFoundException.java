package br.com.postechfiap.toloni.restauranthub.domain.shared.exception;

/// Thrown when a requested resource cannot be found.
///
/// Maps to HTTP 404 Not Found at the presentation layer.
public class NotFoundException extends RuntimeException {
    public NotFoundException(String entityName, Object id) {
        super(entityName + " not found with id: " + id);
    }
}

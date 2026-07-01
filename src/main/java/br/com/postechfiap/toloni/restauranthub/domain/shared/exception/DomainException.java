package br.com.postechfiap.toloni.restauranthub.domain.shared.exception;

/// Base exception for domain rule violations.
///
/// Thrown when a domain invariant or business rule is broken.
/// Maps to HTTP 422 Unprocessable Entity at the presentation layer.
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}

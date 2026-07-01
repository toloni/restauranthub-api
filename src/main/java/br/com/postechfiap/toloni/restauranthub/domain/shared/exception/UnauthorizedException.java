package br.com.postechfiap.toloni.restauranthub.domain.shared.exception;

/// Thrown when a requester attempts an action they are not authorized to perform.
///
/// Maps to HTTP 403 Forbidden at the presentation layer.
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

package br.com.postechfiap.toloni.restauranthub.application.pagination;

import br.com.postechfiap.toloni.restauranthub.domain.shared.exception.DomainException;

public record PageFilter(String field, String value) {

    public PageFilter {
        if (field == null || field.isBlank()) throw new DomainException("Filter field is required.");
        if (value == null || value.isBlank()) throw new DomainException("Filter value is required.");
    }

    public static PageFilter of(String field, String value) {
        return new PageFilter(field, value);
    }
}

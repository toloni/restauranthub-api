package br.com.postechfiap.toloni.restauranthub.application.pagination;

import br.com.postechfiap.toloni.restauranthub.domain.shared.exception.DomainException;

public record PageSort(String field, SortDirection direction) {

    public PageSort {
        if (field == null || field.isBlank()) throw new DomainException("Sort field is required.");
        if (direction == null) throw new DomainException("Sort direction is required.");
    }

    public static PageSort of(String field, SortDirection direction) {
        return new PageSort(field, direction);
    }

    public static PageSort asc(String field) {
        return new PageSort(field, SortDirection.ASC);
    }

    public static PageSort desc(String field) {
        return new PageSort(field, SortDirection.DESC);
    }

    public boolean isAscending() {
        return direction == SortDirection.ASC;
    }
}

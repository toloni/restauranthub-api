package br.com.postechfiap.toloni.restauranthub.application.pagination;

import br.com.postechfiap.toloni.restauranthub.domain.shared.exception.DomainException;

import java.util.List;

public record PageRequest(int pageNumber, int pageSize, List<PageFilter> filters, List<PageSort> sorts) {

    public PageRequest {
        if (pageNumber < 0) throw new DomainException("Page number must not be negative.");
        if (pageSize < 1) throw new DomainException("Page size must be at least 1.");
        filters = filters != null ? List.copyOf(filters) : List.of();
        sorts = sorts != null ? List.copyOf(sorts) : List.of();
    }

    public static PageRequest of(int pageNumber, int pageSize, List<PageFilter> filters, List<PageSort> sorts) {
        return new PageRequest(pageNumber, pageSize, filters, sorts);
    }

    public static PageRequest of(int pageNumber, int pageSize) {
        return new PageRequest(pageNumber, pageSize, null, null);
    }

    public boolean hasFilters() {
        return !filters.isEmpty();
    }

    public boolean hasSorts() {
        return !sorts.isEmpty();
    }
}

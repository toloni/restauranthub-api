package br.com.postechfiap.toloni.restauranthub.application.pagination;

import br.com.postechfiap.toloni.restauranthub.domain.shared.exception.DomainException;

import java.util.List;

/// Represents a pagination request carrying page number, page size, filters, and sorts.
///
/// Used to communicate paging parameters from the presentation layer down to
/// the gateway layer without coupling to any specific framework type.
///
/// @param pageNumber the zero-based index of the requested page
/// @param pageSize   the maximum number of items per page (at least 1)
/// @param filters    the list of [PageFilter] to apply; never null (empty list if absent)
/// @param sorts      the list of [PageSort] to apply; never null (empty list if absent)
public record PageRequest(int pageNumber, int pageSize, List<PageFilter> filters, List<PageSort> sorts) {

    public PageRequest {
        if (pageNumber < 0) throw new DomainException("Page number must not be negative.");
        if (pageSize < 1) throw new DomainException("Page size must be at least 1.");
        filters = filters != null ? List.copyOf(filters) : List.of();
        sorts = sorts != null ? List.copyOf(sorts) : List.of();
    }

    /// Creates a [PageRequest] with filters and sorts.
    ///
    /// @param pageNumber the zero-based page index
    /// @param pageSize   the number of items per page
    /// @param filters    the filters to apply
    /// @param sorts      the sort criteria to apply
    /// @return a new [PageRequest]
    /// @throws DomainException if `pageNumber` is negative or `pageSize` is less than 1
    public static PageRequest of(int pageNumber, int pageSize, List<PageFilter> filters, List<PageSort> sorts) {
        return new PageRequest(pageNumber, pageSize, filters, sorts);
    }

    /// Creates a [PageRequest] without filters or sorts.
    ///
    /// @param pageNumber the zero-based page index
    /// @param pageSize   the number of items per page
    /// @return a new [PageRequest]
    /// @throws DomainException if `pageNumber` is negative or `pageSize` is less than 1
    public static PageRequest of(int pageNumber, int pageSize) {
        return new PageRequest(pageNumber, pageSize, null, null);
    }

    /// @return `true` if this request carries at least one filter
    public boolean hasFilters() {
        return !filters.isEmpty();
    }

    /// @return `true` if this request carries at least one sort criterion
    public boolean hasSorts() {
        return !sorts.isEmpty();
    }
}

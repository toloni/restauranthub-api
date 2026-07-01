package br.com.postechfiap.toloni.restauranthub.application.pagination;

import br.com.postechfiap.toloni.restauranthub.domain.shared.exception.DomainException;

/// Represents a single sort criterion for paginated queries.
///
/// Combines a field name with a [SortDirection], both of which are mandatory
/// and validated on construction.
///
/// @param field     the name of the field to sort by
/// @param direction the [SortDirection] indicating ascending or descending order
public record PageSort(String field, SortDirection direction) {

    public PageSort {
        if (field == null || field.isBlank()) throw new DomainException("Sort field is required.");
        if (direction == null) throw new DomainException("Sort direction is required.");
    }

    /// Creates a [PageSort] with the given field and direction.
    ///
    /// @param field     the name of the field to sort by
    /// @param direction the [SortDirection]
    /// @return a new [PageSort]
    /// @throws DomainException if `field` is null or blank, or `direction` is null
    public static PageSort of(String field, SortDirection direction) {
        return new PageSort(field, direction);
    }

    /// Creates an ascending [PageSort] for the given field.
    ///
    /// @param field the name of the field to sort by
    /// @return a new [PageSort] with [SortDirection#ASC]
    public static PageSort asc(String field) {
        return new PageSort(field, SortDirection.ASC);
    }

    /// Creates a descending [PageSort] for the given field.
    ///
    /// @param field the name of the field to sort by
    /// @return a new [PageSort] with [SortDirection#DESC]
    public static PageSort desc(String field) {
        return new PageSort(field, SortDirection.DESC);
    }

    /// @return `true` if the sort direction is ascending
    public boolean isAscending() {
        return direction == SortDirection.ASC;
    }
}

package br.com.postechfiap.toloni.restauranthub.application.pagination;

import br.com.postechfiap.toloni.restauranthub.domain.shared.exception.DomainException;

/// Represents a single filter criterion for paginated queries.
///
/// Combines a field name with a value used to restrict query results.
/// Both components are mandatory and validated on construction.
///
/// @param field the name of the field to filter by
/// @param value the value to match against the field
public record PageFilter(String field, String value) {

    public PageFilter {
        if (field == null || field.isBlank()) throw new DomainException("Filter field is required.");
        if (value == null || value.isBlank()) throw new DomainException("Filter value is required.");
    }

    /// Creates a [PageFilter] with the given field and value.
    ///
    /// @param field the name of the field to filter by
    /// @param value the value to match against the field
    /// @return a new [PageFilter]
    /// @throws DomainException if `field` or `value` is null or blank
    public static PageFilter of(String field, String value) {
        return new PageFilter(field, value);
    }
}

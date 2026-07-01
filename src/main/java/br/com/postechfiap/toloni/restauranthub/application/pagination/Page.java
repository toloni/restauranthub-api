package br.com.postechfiap.toloni.restauranthub.application.pagination;

import java.util.List;

/// Represents a paginated result set.
///
/// Wraps a list of content items alongside pagination metadata such as page number,
/// page size, and total number of elements across all pages.
///
/// @param <T>           the type of the content items
/// @param content       the items on this page (never null; empty list if there is no content)
/// @param pageNumber    the zero-based index of this page
/// @param pageSize      the maximum number of items per page
/// @param totalElements the total number of elements across all pages
public record Page<T>(List<T> content, int pageNumber, int pageSize, long totalElements) {

    public Page {
        content = content != null ? List.copyOf(content) : List.of();
    }

    /// Creates a [Page] with the given content and pagination metadata.
    ///
    /// @param content       the items on this page
    /// @param pageNumber    the zero-based index of this page
    /// @param pageSize      the maximum number of items per page
    /// @param totalElements the total number of elements across all pages
    /// @param <T>           the type of the content items
    /// @return a new [Page]
    public static <T> Page<T> of(List<T> content, int pageNumber, int pageSize, long totalElements) {
        return new Page<>(content, pageNumber, pageSize, totalElements);
    }

    /// Creates an empty [Page] with zero elements.
    ///
    /// @param <T> the type of the content items
    /// @return an empty [Page]
    public static <T> Page<T> empty() {
        return new Page<>(List.of(), 0, 0, 0L);
    }

    /// @return the total number of pages based on the page size and total elements
    public int getTotalPages() {
        return pageSize > 0 ? (int) Math.ceil((double) totalElements / pageSize) : 0;
    }

    /// @return `true` if this page contains no items
    public boolean isEmpty() {
        return content.isEmpty();
    }

    /// @return `true` if this is the first page (page number 0)
    public boolean isFirst() {
        return pageNumber == 0;
    }

    /// @return `true` if this is the last page
    public boolean isLast() {
        return pageNumber >= getTotalPages() - 1;
    }

    /// @return `true` if there is a next page after this one
    public boolean hasNext() {
        return pageNumber < getTotalPages() - 1;
    }

    /// @return `true` if there is a previous page before this one
    public boolean hasPrevious() {
        return pageNumber > 0;
    }
}

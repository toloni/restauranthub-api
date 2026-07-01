package br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.shared;

import br.com.postechfiap.toloni.restauranthub.application.pagination.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

/// Utility class for converting domain [PageRequest] to Spring Data [org.springframework.data.domain.PageRequest].
public final class PageRequestMapper {

    private PageRequestMapper() {
    }

    /// Converts a domain [PageRequest] to a Spring Data [org.springframework.data.domain.PageRequest].
    ///
    /// @param pageRequest the domain [PageRequest] to convert
    /// @return a Spring Data [org.springframework.data.domain.PageRequest]
    public static org.springframework.data.domain.PageRequest toPageable(PageRequest pageRequest) {
        var sort = pageRequest.hasSorts()
                ? Sort.by(pageRequest.sorts().stream()
                .map(s -> s.isAscending() ? Order.asc(s.field()) : Order.desc(s.field()))
                .toList())
                : Sort.unsorted();

        return org.springframework.data.domain.PageRequest.of(
                pageRequest.pageNumber(),
                pageRequest.pageSize(),
                sort
        );
    }
}


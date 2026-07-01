package br.com.postechfiap.toloni.restauranthub.adapters.shared;

import br.com.postechfiap.toloni.restauranthub.application.pagination.PageRequest;
import br.com.postechfiap.toloni.restauranthub.application.pagination.PageSort;
import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.shared.PageRequestMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
class PageRequestMapperTest {

    // -------------------------------------------------------------------------
    // Without sorts
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should convert PageRequest without sorts to unsorted Pageable")
    void shouldConvertPageRequestWithoutSortsToUnsortedPageable() {
        var pageRequest = PageRequest.of(0, 10);

        var pageable = PageRequestMapper.toPageable(pageRequest);

        assertThat(pageable.getPageNumber()).isZero();
        assertThat(pageable.getPageSize()).isEqualTo(10);
        assertThat(pageable.getSort().isUnsorted()).isTrue();
    }

    @Test
    @DisplayName("Should convert PageRequest with empty sorts list to unsorted Pageable")
    void shouldConvertPageRequestWithEmptySortsListToUnsortedPageable() {
        var pageRequest = PageRequest.of(0, 10, List.of(), List.of());

        var pageable = PageRequestMapper.toPageable(pageRequest);

        assertThat(pageable.getSort().isUnsorted()).isTrue();
    }

    // -------------------------------------------------------------------------
    // With single sort
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should convert PageRequest with single ascending sort")
    void shouldConvertPageRequestWithSingleAscendingSort() {
        var pageRequest = PageRequest.of(0, 10, List.of(), List.of(PageSort.asc("name")));

        var pageable = PageRequestMapper.toPageable(pageRequest);

        assertThat(pageable.getSort().isSorted()).isTrue();
        var order = pageable.getSort().getOrderFor("name");
        assertThat(order).isNotNull();
        assertThat(order.getDirection()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    @DisplayName("Should convert PageRequest with single descending sort")
    void shouldConvertPageRequestWithSingleDescendingSort() {
        var pageRequest = PageRequest.of(0, 10, List.of(), List.of(PageSort.desc("name")));

        var pageable = PageRequestMapper.toPageable(pageRequest);

        var order = pageable.getSort().getOrderFor("name");
        assertThat(order).isNotNull();
        assertThat(order.getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    // -------------------------------------------------------------------------
    // With multiple sorts
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should convert PageRequest with multiple sorts preserving order")
    void shouldConvertPageRequestWithMultipleSortsPreservingOrder() {
        var pageRequest = PageRequest.of(0, 10, List.of(),
                List.of(PageSort.asc("name"), PageSort.desc("price")));

        var pageable = PageRequestMapper.toPageable(pageRequest);

        var orders = pageable.getSort().toList();
        assertThat(orders).hasSize(2);
        assertThat(orders.get(0).getProperty()).isEqualTo("name");
        assertThat(orders.get(0).getDirection()).isEqualTo(Sort.Direction.ASC);
        assertThat(orders.get(1).getProperty()).isEqualTo("price");
        assertThat(orders.get(1).getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    @DisplayName("Should convert PageRequest with multiple ascending sorts")
    void shouldConvertPageRequestWithMultipleAscendingSorts() {
        var pageRequest = PageRequest.of(0, 10, List.of(),
                List.of(PageSort.asc("name"), PageSort.asc("price")));

        var pageable = PageRequestMapper.toPageable(pageRequest);

        var orders = pageable.getSort().toList();
        assertThat(orders).allMatch(order -> order.getDirection() == Sort.Direction.ASC);
    }

    // -------------------------------------------------------------------------
    // Page number and size
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should preserve page number and size")
    void shouldPreservePageNumberAndSize() {
        var pageRequest = PageRequest.of(2, 25);

        var pageable = PageRequestMapper.toPageable(pageRequest);

        assertThat(pageable.getPageNumber()).isEqualTo(2);
        assertThat(pageable.getPageSize()).isEqualTo(25);
    }

    @Test
    @DisplayName("Should convert first page correctly")
    void shouldConvertFirstPageCorrectly() {
        var pageRequest = PageRequest.of(0, 1);

        var pageable = PageRequestMapper.toPageable(pageRequest);

        assertThat(pageable.getPageNumber()).isZero();
        assertThat(pageable.getPageSize()).isEqualTo(1);
    }
}
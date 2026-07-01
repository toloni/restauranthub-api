package br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.shared;

import br.com.postechfiap.toloni.restauranthub.application.pagination.PageFilter;
import br.com.postechfiap.toloni.restauranthub.domain.usertype.UserRole;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class JpaSpecificationBuilderTest {

    @Mock
    private Root<Object> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Path<Object> path;

    @Mock
    private Expression<String> stringExpression;

    @Mock
    private Predicate predicate;

    // -------------------------------------------------------------------------
    // fromFilters - empty
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should return conjunction predicate when filters list is empty")
    void shouldReturnConjunctionPredicateWhenFiltersListIsEmpty() {
        when(criteriaBuilder.conjunction()).thenReturn(predicate);

        var specification = JpaSpecificationBuilder.<Object>fromFilters(List.of());
        var result = specification.toPredicate(root, query, criteriaBuilder);

        assertThat(result).isEqualTo(predicate);
        verify(criteriaBuilder, times(1)).conjunction();
    }

    // -------------------------------------------------------------------------
    // fromFilters - String field
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should apply LIKE filter for String field")
    void shouldApplyLikeFilterForStringField() {
        var filter = PageFilter.of("name", "Burger");

        doReturn(path).when(root).get("name");
        doReturn((Class) String.class).when(path).getJavaType();
        doReturn(stringExpression).when(path).as(String.class);
        when(criteriaBuilder.lower(stringExpression)).thenReturn(stringExpression);
        when(criteriaBuilder.like(stringExpression, "%burger%")).thenReturn(predicate);

        var specification = JpaSpecificationBuilder.<Object>fromFilters(List.of(filter));
        var result = specification.toPredicate(root, query, criteriaBuilder);

        assertThat(result).isEqualTo(predicate);
        verify(criteriaBuilder, times(1)).like(stringExpression, "%burger%");
    }

    @Test
    @DisplayName("Should normalize filter value to lowercase for String field")
    void shouldNormalizeFilterValueToLowercaseForStringField() {
        var filter = PageFilter.of("name", "BURGER");

        doReturn(path).when(root).get("name");
        doReturn((Class) String.class).when(path).getJavaType();
        doReturn(stringExpression).when(path).as(String.class);
        when(criteriaBuilder.lower(stringExpression)).thenReturn(stringExpression);
        when(criteriaBuilder.like(stringExpression, "%burger%")).thenReturn(predicate);

        var specification = JpaSpecificationBuilder.<Object>fromFilters(List.of(filter));
        specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder, times(1)).like(stringExpression, "%burger%");
    }

    // -------------------------------------------------------------------------
    // fromFilters - UUID field
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should apply equality filter for valid UUID field")
    void shouldApplyEqualityFilterForValidUUIDField() {
        var uuid = UUID.randomUUID();
        var filter = PageFilter.of("id", uuid.toString());

        doReturn(path).when(root).get("id");
        doReturn((Class) UUID.class).when(path).getJavaType();
        when(criteriaBuilder.equal(path, uuid)).thenReturn(predicate);

        var specification = JpaSpecificationBuilder.<Object>fromFilters(List.of(filter));
        var result = specification.toPredicate(root, query, criteriaBuilder);

        assertThat(result).isEqualTo(predicate);
        verify(criteriaBuilder, times(1)).equal(path, uuid);
    }

    // -------------------------------------------------------------------------
    // fromFilters - Enum field
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should apply equality filter for valid Enum field")
    void shouldApplyEqualityFilterForValidEnumField() {
        var filter = PageFilter.of("role", "restaurant_owner");

        doReturn(path).when(root).get("role");
        doReturn((Class) UserRole.class).when(path).getJavaType();
        when(criteriaBuilder.equal(path, UserRole.RESTAURANT_OWNER)).thenReturn(predicate);

        var specification = JpaSpecificationBuilder.<Object>fromFilters(List.of(filter));
        var result = specification.toPredicate(root, query, criteriaBuilder);

        assertThat(result).isEqualTo(predicate);
        verify(criteriaBuilder, times(1)).equal(path, UserRole.RESTAURANT_OWNER);
    }

    // -------------------------------------------------------------------------
    // fromFilters - multiple filters
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should combine multiple filters with AND")
    void shouldCombineMultipleFiltersWithAnd() {
        var nameFilter = PageFilter.of("name", "Burger");
        var idFilter = PageFilter.of("id", UUID.randomUUID().toString());

        var namePath = mock(Path.class);
        var idPath = mock(Path.class);
        var combinedPredicate = mock(Predicate.class);

        doReturn(namePath).when(root).get("name");
        doReturn((Class) String.class).when(namePath).getJavaType();
        doReturn(stringExpression).when(namePath).as(String.class);
        when(criteriaBuilder.lower(stringExpression)).thenReturn(stringExpression);
        when(criteriaBuilder.like(eq(stringExpression), anyString())).thenReturn(predicate);

        doReturn(idPath).when(root).get("id");
        doReturn((Class) UUID.class).when(idPath).getJavaType();
        when(criteriaBuilder.equal(eq(idPath), any(UUID.class))).thenReturn(predicate);

        when(criteriaBuilder.and(any(Predicate.class), any(Predicate.class))).thenReturn(combinedPredicate);

        var specification = JpaSpecificationBuilder.<Object>fromFilters(List.of(nameFilter, idFilter));
        var result = specification.toPredicate(root, query, criteriaBuilder);

        assertThat(result).isEqualTo(combinedPredicate);
    }
}
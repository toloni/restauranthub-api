package br.com.postechfiap.toloni.restauranthub.domain.restaurant.valueobject;

import br.com.postechfiap.toloni.restauranthub.domain.shared.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class RestaurantCuisineTypeTest {

    @Test
    @DisplayName("should create RestaurantCuisineType with valid value")
    void shouldCreateWithValidValue() {
        var type = RestaurantCuisineType.of("Italian");
        assertThat(type.getValue()).isEqualTo("Italian");
    }

    @Test
    @DisplayName("should throw DomainException when value is null")
    void shouldThrowWhenNull() {
        assertThatThrownBy(() -> RestaurantCuisineType.of(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("Restaurant cuisine type is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is blank")
    void shouldThrowWhenBlank() {
        assertThatThrownBy(() -> RestaurantCuisineType.of("   "))
                .isInstanceOf(DomainException.class)
                .hasMessage("Restaurant cuisine type is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is empty")
    void shouldThrowWhenEmpty() {
        assertThatThrownBy(() -> RestaurantCuisineType.of(""))
                .isInstanceOf(DomainException.class)
                .hasMessage("Restaurant cuisine type is required.");
    }

    @Test
    @DisplayName("should be equal when same value")
    void shouldBeEqualWhenSameValue() {
        var a = RestaurantCuisineType.of("Italian");
        var b = RestaurantCuisineType.of("Italian");
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("should not be equal when different value")
    void shouldNotBeEqualWhenDifferentValue() {
        var a = RestaurantCuisineType.of("Italian");
        var b = RestaurantCuisineType.of("Japanese");
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    @DisplayName("toString should return the value")
    void toStringShouldReturnValue() {
        var type = RestaurantCuisineType.of("Italian");
        assertThat(type.toString()).isEqualTo("Italian");
    }
}

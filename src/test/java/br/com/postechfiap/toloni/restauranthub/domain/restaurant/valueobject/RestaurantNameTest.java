package br.com.postechfiap.toloni.restauranthub.domain.restaurant.valueobject;

import br.com.postechfiap.toloni.restauranthub.domain.shared.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class RestaurantNameTest {

    @Test
    @DisplayName("should create RestaurantName with valid value")
    void shouldCreateWithValidValue() {
        var name = RestaurantName.of("The Great Burger");
        assertThat(name.getValue()).isEqualTo("The Great Burger");
    }

    @Test
    @DisplayName("should throw DomainException when value is null")
    void shouldThrowWhenNull() {
        assertThatThrownBy(() -> RestaurantName.of(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("Restaurant name is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is blank")
    void shouldThrowWhenBlank() {
        assertThatThrownBy(() -> RestaurantName.of("   "))
                .isInstanceOf(DomainException.class)
                .hasMessage("Restaurant name is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is empty")
    void shouldThrowWhenEmpty() {
        assertThatThrownBy(() -> RestaurantName.of(""))
                .isInstanceOf(DomainException.class)
                .hasMessage("Restaurant name is required.");
    }

    @Test
    @DisplayName("should be equal when same value")
    void shouldBeEqualWhenSameValue() {
        var a = RestaurantName.of("Burger Place");
        var b = RestaurantName.of("Burger Place");
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("should not be equal when different value")
    void shouldNotBeEqualWhenDifferentValue() {
        var a = RestaurantName.of("Burger Place");
        var b = RestaurantName.of("Pizza Palace");
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    @DisplayName("toString should return the value")
    void toStringShouldReturnValue() {
        var name = RestaurantName.of("The Great Burger");
        assertThat(name.toString()).isEqualTo("The Great Burger");
    }
}

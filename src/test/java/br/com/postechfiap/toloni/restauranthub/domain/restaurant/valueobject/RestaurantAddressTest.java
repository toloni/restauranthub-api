package br.com.postechfiap.toloni.restauranthub.domain.restaurant.valueobject;

import br.com.postechfiap.toloni.restauranthub.domain.shared.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class RestaurantAddressTest {

    @Test
    @DisplayName("should create RestaurantAddress with valid value")
    void shouldCreateWithValidValue() {
        var address = RestaurantAddress.of("123 Main St, Springfield");
        assertThat(address.getValue()).isEqualTo("123 Main St, Springfield");
    }

    @Test
    @DisplayName("should throw DomainException when value is null")
    void shouldThrowWhenNull() {
        assertThatThrownBy(() -> RestaurantAddress.of(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("Restaurant address is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is blank")
    void shouldThrowWhenBlank() {
        assertThatThrownBy(() -> RestaurantAddress.of("   "))
                .isInstanceOf(DomainException.class)
                .hasMessage("Restaurant address is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is empty")
    void shouldThrowWhenEmpty() {
        assertThatThrownBy(() -> RestaurantAddress.of(""))
                .isInstanceOf(DomainException.class)
                .hasMessage("Restaurant address is required.");
    }

    @Test
    @DisplayName("should be equal when same value")
    void shouldBeEqualWhenSameValue() {
        var a = RestaurantAddress.of("123 Main St");
        var b = RestaurantAddress.of("123 Main St");
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("should not be equal when different value")
    void shouldNotBeEqualWhenDifferentValue() {
        var a = RestaurantAddress.of("123 Main St");
        var b = RestaurantAddress.of("456 Oak Ave");
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    @DisplayName("toString should return the value")
    void toStringShouldReturnValue() {
        var address = RestaurantAddress.of("123 Main St");
        assertThat(address.toString()).isEqualTo("123 Main St");
    }
}

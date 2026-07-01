package br.com.postechfiap.toloni.restauranthub.domain.restaurant.valueobject;

import br.com.postechfiap.toloni.restauranthub.domain.shared.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class RestaurantOpeningHoursTest {

    @Test
    @DisplayName("should create RestaurantOpeningHours with valid value")
    void shouldCreateWithValidValue() {
        var hours = RestaurantOpeningHours.of("Mon-Fri 9am-10pm");
        assertThat(hours.getValue()).isEqualTo("Mon-Fri 9am-10pm");
    }

    @Test
    @DisplayName("should throw DomainException when value is null")
    void shouldThrowWhenNull() {
        assertThatThrownBy(() -> RestaurantOpeningHours.of(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("Restaurant opening hours is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is blank")
    void shouldThrowWhenBlank() {
        assertThatThrownBy(() -> RestaurantOpeningHours.of("  "))
                .isInstanceOf(DomainException.class)
                .hasMessage("Restaurant opening hours is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is empty")
    void shouldThrowWhenEmpty() {
        assertThatThrownBy(() -> RestaurantOpeningHours.of(""))
                .isInstanceOf(DomainException.class)
                .hasMessage("Restaurant opening hours is required.");
    }

    @Test
    @DisplayName("should be equal when same value")
    void shouldBeEqualWhenSameValue() {
        var a = RestaurantOpeningHours.of("Mon-Fri 9am-10pm");
        var b = RestaurantOpeningHours.of("Mon-Fri 9am-10pm");
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("should not be equal when different value")
    void shouldNotBeEqualWhenDifferentValue() {
        var a = RestaurantOpeningHours.of("Mon-Fri 9am-10pm");
        var b = RestaurantOpeningHours.of("Sat-Sun 10am-8pm");
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    @DisplayName("toString should return the value")
    void toStringShouldReturnValue() {
        var hours = RestaurantOpeningHours.of("Mon-Fri 9am-10pm");
        assertThat(hours.toString()).isEqualTo("Mon-Fri 9am-10pm");
    }
}

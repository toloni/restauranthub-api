package br.com.postechfiap.toloni.restauranthub.domain.usertype.valueobject;

import br.com.postechfiap.toloni.restauranthub.domain.shared.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class UserTypeDescriptionTest {

    @Test
    @DisplayName("should create UserTypeDescription with valid value")
    void shouldCreateWithValidValue() {
        var desc = UserTypeDescription.of("Owns and manages a restaurant");
        assertThat(desc.getValue()).isEqualTo("Owns and manages a restaurant");
    }

    @Test
    @DisplayName("should throw DomainException when value is null")
    void shouldThrowWhenNull() {
        assertThatThrownBy(() -> UserTypeDescription.of(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("UserType description is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is blank")
    void shouldThrowWhenBlank() {
        assertThatThrownBy(() -> UserTypeDescription.of("   "))
                .isInstanceOf(DomainException.class)
                .hasMessage("UserType description is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is empty")
    void shouldThrowWhenEmpty() {
        assertThatThrownBy(() -> UserTypeDescription.of(""))
                .isInstanceOf(DomainException.class)
                .hasMessage("UserType description is required.");
    }

    @Test
    @DisplayName("should be equal when same value")
    void shouldBeEqualWhenSameValue() {
        var a = UserTypeDescription.of("Owns and manages a restaurant");
        var b = UserTypeDescription.of("Owns and manages a restaurant");
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("should not be equal when different value")
    void shouldNotBeEqualWhenDifferentValue() {
        var a = UserTypeDescription.of("Owns and manages a restaurant");
        var b = UserTypeDescription.of("Regular customer");
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    @DisplayName("toString should return the value")
    void toStringShouldReturnValue() {
        var desc = UserTypeDescription.of("Owns and manages a restaurant");
        assertThat(desc.toString()).isEqualTo("Owns and manages a restaurant");
    }
}

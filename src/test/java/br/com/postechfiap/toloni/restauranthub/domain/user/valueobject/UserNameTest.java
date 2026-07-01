package br.com.postechfiap.toloni.restauranthub.domain.user.valueobject;

import br.com.postechfiap.toloni.restauranthub.domain.shared.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class UserNameTest {

    @Test
    @DisplayName("should create UserName with valid value")
    void shouldCreateWithValidValue() {
        var name = UserName.of("John Doe");
        assertThat(name.getValue()).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("should throw DomainException when value is null")
    void shouldThrowWhenNull() {
        assertThatThrownBy(() -> UserName.of(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("User name is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is blank")
    void shouldThrowWhenBlank() {
        assertThatThrownBy(() -> UserName.of("   "))
                .isInstanceOf(DomainException.class)
                .hasMessage("User name is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is empty")
    void shouldThrowWhenEmpty() {
        assertThatThrownBy(() -> UserName.of(""))
                .isInstanceOf(DomainException.class)
                .hasMessage("User name is required.");
    }

    @Test
    @DisplayName("should be equal when same value")
    void shouldBeEqualWhenSameValue() {
        var a = UserName.of("John Doe");
        var b = UserName.of("John Doe");
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("should not be equal when different value")
    void shouldNotBeEqualWhenDifferentValue() {
        var a = UserName.of("John Doe");
        var b = UserName.of("Jane Doe");
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    @DisplayName("toString should return the value")
    void toStringShouldReturnValue() {
        var name = UserName.of("John Doe");
        assertThat(name.toString()).isEqualTo("John Doe");
    }
}

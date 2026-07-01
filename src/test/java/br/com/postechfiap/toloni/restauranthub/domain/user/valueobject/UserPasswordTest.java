package br.com.postechfiap.toloni.restauranthub.domain.user.valueobject;

import br.com.postechfiap.toloni.restauranthub.domain.shared.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class UserPasswordTest {

    @Test
    @DisplayName("should create UserPassword with valid value")
    void shouldCreateWithValidValue() {
        var password = UserPassword.of("secret123");
        assertThat(password.getValue()).isEqualTo("secret123");
    }

    @Test
    @DisplayName("should create UserPassword with exactly 8 characters")
    void shouldCreateWithExactlyEightChars() {
        var password = UserPassword.of("12345678");
        assertThat(password.getValue()).isEqualTo("12345678");
    }

    @Test
    @DisplayName("should throw DomainException when value is null")
    void shouldThrowWhenNull() {
        assertThatThrownBy(() -> UserPassword.of(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("User password is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is blank")
    void shouldThrowWhenBlank() {
        assertThatThrownBy(() -> UserPassword.of("   "))
                .isInstanceOf(DomainException.class)
                .hasMessage("User password is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is empty")
    void shouldThrowWhenEmpty() {
        assertThatThrownBy(() -> UserPassword.of(""))
                .isInstanceOf(DomainException.class)
                .hasMessage("User password is required.");
    }

    @Test
    @DisplayName("should throw DomainException when password is shorter than 8 characters")
    void shouldThrowWhenTooShort() {
        assertThatThrownBy(() -> UserPassword.of("1234567"))
                .isInstanceOf(DomainException.class)
                .hasMessage("User password must be at least 8 characters.");
    }

    @Test
    @DisplayName("should be equal when same value")
    void shouldBeEqualWhenSameValue() {
        var a = UserPassword.of("secret123");
        var b = UserPassword.of("secret123");
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("should not be equal when different value")
    void shouldNotBeEqualWhenDifferentValue() {
        var a = UserPassword.of("secret123");
        var b = UserPassword.of("different456");
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    @DisplayName("toString should return masked value")
    void toStringShouldReturnMaskedValue() {
        var password = UserPassword.of("secret123");
        assertThat(password.toString()).isEqualTo("***");
    }
}

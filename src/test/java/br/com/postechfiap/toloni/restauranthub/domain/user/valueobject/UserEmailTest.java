package br.com.postechfiap.toloni.restauranthub.domain.user.valueobject;

import br.com.postechfiap.toloni.restauranthub.domain.shared.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class UserEmailTest {

    @Test
    @DisplayName("should create UserEmail with valid value")
    void shouldCreateWithValidValue() {
        var email = UserEmail.of("john@example.com");
        assertThat(email.getValue()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("should normalize email to lowercase")
    void shouldNormalizeToLowercase() {
        var email = UserEmail.of("John.Doe@Example.COM");
        assertThat(email.getValue()).isEqualTo("john.doe@example.com");
    }

    @Test
    @DisplayName("should throw DomainException when value is null")
    void shouldThrowWhenNull() {
        assertThatThrownBy(() -> UserEmail.of(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("User email is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is blank")
    void shouldThrowWhenBlank() {
        assertThatThrownBy(() -> UserEmail.of("   "))
                .isInstanceOf(DomainException.class)
                .hasMessage("User email is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is empty")
    void shouldThrowWhenEmpty() {
        assertThatThrownBy(() -> UserEmail.of(""))
                .isInstanceOf(DomainException.class)
                .hasMessage("User email is required.");
    }

    @Test
    @DisplayName("should throw DomainException when email format is invalid - missing @")
    void shouldThrowWhenMissingAt() {
        assertThatThrownBy(() -> UserEmail.of("johnexample.com"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("User email is invalid");
    }

    @Test
    @DisplayName("should throw DomainException when email format is invalid - missing domain")
    void shouldThrowWhenMissingDomain() {
        assertThatThrownBy(() -> UserEmail.of("john@"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("User email is invalid");
    }

    @Test
    @DisplayName("should throw DomainException when email format is invalid - missing TLD")
    void shouldThrowWhenMissingTLD() {
        assertThatThrownBy(() -> UserEmail.of("john@example"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("User email is invalid");
    }

    @Test
    @DisplayName("should be equal when same value")
    void shouldBeEqualWhenSameValue() {
        var a = UserEmail.of("john@example.com");
        var b = UserEmail.of("john@example.com");
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("should be equal regardless of original casing")
    void shouldBeEqualRegardlessOfCasing() {
        var a = UserEmail.of("John@Example.com");
        var b = UserEmail.of("john@example.com");
        assertThat(a).isEqualTo(b);
    }

    @Test
    @DisplayName("should not be equal when different value")
    void shouldNotBeEqualWhenDifferentValue() {
        var a = UserEmail.of("john@example.com");
        var b = UserEmail.of("jane@example.com");
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    @DisplayName("toString should return the lowercase email")
    void toStringShouldReturnLowercaseEmail() {
        var email = UserEmail.of("John@Example.com");
        assertThat(email.toString()).isEqualTo("john@example.com");
    }
}

package br.com.postechfiap.toloni.restauranthub.domain.usertype.valueobject;

import br.com.postechfiap.toloni.restauranthub.domain.shared.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class UserTypeNameTest {

    @Test
    @DisplayName("should create UserTypeName with valid value")
    void shouldCreateWithValidValue() {
        var name = UserTypeName.of("Restaurant Owner");
        assertThat(name.getValue()).isEqualTo("Restaurant Owner");
    }

    @Test
    @DisplayName("should throw DomainException when value is null")
    void shouldThrowWhenNull() {
        assertThatThrownBy(() -> UserTypeName.of(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("UserType name is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is blank")
    void shouldThrowWhenBlank() {
        assertThatThrownBy(() -> UserTypeName.of("   "))
                .isInstanceOf(DomainException.class)
                .hasMessage("UserType name is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is empty")
    void shouldThrowWhenEmpty() {
        assertThatThrownBy(() -> UserTypeName.of(""))
                .isInstanceOf(DomainException.class)
                .hasMessage("UserType name is required.");
    }

    @Test
    @DisplayName("should be equal when same value")
    void shouldBeEqualWhenSameValue() {
        var a = UserTypeName.of("Restaurant Owner");
        var b = UserTypeName.of("Restaurant Owner");
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("should not be equal when different value")
    void shouldNotBeEqualWhenDifferentValue() {
        var a = UserTypeName.of("Restaurant Owner");
        var b = UserTypeName.of("Customer");
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    @DisplayName("toString should return the value")
    void toStringShouldReturnValue() {
        var name = UserTypeName.of("Restaurant Owner");
        assertThat(name.toString()).isEqualTo("Restaurant Owner");
    }
}

package br.com.postechfiap.toloni.restauranthub.domain.menuitem.valueobject;

import br.com.postechfiap.toloni.restauranthub.domain.shared.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class MenuItemNameTest {

    @Test
    @DisplayName("should create MenuItemName with valid value")
    void shouldCreateWithValidValue() {
        var name = MenuItemName.of("Margherita Pizza");
        assertThat(name.getValue()).isEqualTo("Margherita Pizza");
    }

    @Test
    @DisplayName("should throw DomainException when value is null")
    void shouldThrowWhenNull() {
        assertThatThrownBy(() -> MenuItemName.of(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("MenuItem name is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is blank")
    void shouldThrowWhenBlank() {
        assertThatThrownBy(() -> MenuItemName.of("   "))
                .isInstanceOf(DomainException.class)
                .hasMessage("MenuItem name is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is empty")
    void shouldThrowWhenEmpty() {
        assertThatThrownBy(() -> MenuItemName.of(""))
                .isInstanceOf(DomainException.class)
                .hasMessage("MenuItem name is required.");
    }

    @Test
    @DisplayName("should be equal when same value")
    void shouldBeEqualWhenSameValue() {
        var a = MenuItemName.of("Margherita Pizza");
        var b = MenuItemName.of("Margherita Pizza");
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("should not be equal when different value")
    void shouldNotBeEqualWhenDifferentValue() {
        var a = MenuItemName.of("Margherita Pizza");
        var b = MenuItemName.of("Pepperoni Pizza");
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    @DisplayName("toString should return the value")
    void toStringShouldReturnValue() {
        var name = MenuItemName.of("Margherita Pizza");
        assertThat(name.toString()).isEqualTo("Margherita Pizza");
    }
}

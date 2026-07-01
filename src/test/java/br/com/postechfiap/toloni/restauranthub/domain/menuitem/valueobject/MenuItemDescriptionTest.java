package br.com.postechfiap.toloni.restauranthub.domain.menuitem.valueobject;

import br.com.postechfiap.toloni.restauranthub.domain.shared.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class MenuItemDescriptionTest {

    @Test
    @DisplayName("should create MenuItemDescription with valid value")
    void shouldCreateWithValidValue() {
        var desc = MenuItemDescription.of("Classic Italian pizza with tomato and mozzarella");
        assertThat(desc.getValue()).isEqualTo("Classic Italian pizza with tomato and mozzarella");
    }

    @Test
    @DisplayName("should throw DomainException when value is null")
    void shouldThrowWhenNull() {
        assertThatThrownBy(() -> MenuItemDescription.of(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("MenuItem description is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is blank")
    void shouldThrowWhenBlank() {
        assertThatThrownBy(() -> MenuItemDescription.of("   "))
                .isInstanceOf(DomainException.class)
                .hasMessage("MenuItem description is required.");
    }

    @Test
    @DisplayName("should throw DomainException when value is empty")
    void shouldThrowWhenEmpty() {
        assertThatThrownBy(() -> MenuItemDescription.of(""))
                .isInstanceOf(DomainException.class)
                .hasMessage("MenuItem description is required.");
    }

    @Test
    @DisplayName("should be equal when same value")
    void shouldBeEqualWhenSameValue() {
        var a = MenuItemDescription.of("Delicious pizza");
        var b = MenuItemDescription.of("Delicious pizza");
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("should not be equal when different value")
    void shouldNotBeEqualWhenDifferentValue() {
        var a = MenuItemDescription.of("Delicious pizza");
        var b = MenuItemDescription.of("Crispy burger");
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    @DisplayName("toString should return the value")
    void toStringShouldReturnValue() {
        var desc = MenuItemDescription.of("Delicious pizza");
        assertThat(desc.toString()).isEqualTo("Delicious pizza");
    }
}

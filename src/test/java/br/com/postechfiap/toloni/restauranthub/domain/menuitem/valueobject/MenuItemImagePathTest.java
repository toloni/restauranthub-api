package br.com.postechfiap.toloni.restauranthub.domain.menuitem.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
class MenuItemImagePathTest {

    @Test
    @DisplayName("should create MenuItemImagePath with valid value")
    void shouldCreateWithValidValue() {
        var path = MenuItemImagePath.of("/images/pizza.jpg");
        assertThat(path.getValue()).isEqualTo("/images/pizza.jpg");
        assertThat(path.isPresent()).isTrue();
    }

    @Test
    @DisplayName("should create MenuItemImagePath with null value")
    void shouldCreateWithNull() {
        var path = MenuItemImagePath.of(null);
        assertThat(path.getValue()).isNull();
        assertThat(path.isPresent()).isFalse();
    }

    @Test
    @DisplayName("should report not present when value is blank")
    void shouldReportNotPresentWhenBlank() {
        var path = MenuItemImagePath.of("   ");
        assertThat(path.isPresent()).isFalse();
    }

    @Test
    @DisplayName("should report not present when value is empty")
    void shouldReportNotPresentWhenEmpty() {
        var path = MenuItemImagePath.of("");
        assertThat(path.isPresent()).isFalse();
    }

    @Test
    @DisplayName("should be equal when same value")
    void shouldBeEqualWhenSameValue() {
        var a = MenuItemImagePath.of("/images/pizza.jpg");
        var b = MenuItemImagePath.of("/images/pizza.jpg");
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("should not be equal when different value")
    void shouldNotBeEqualWhenDifferentValue() {
        var a = MenuItemImagePath.of("/images/pizza.jpg");
        var b = MenuItemImagePath.of("/images/burger.jpg");
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    @DisplayName("toString should return empty string when null")
    void toStringShouldReturnEmptyWhenNull() {
        var path = MenuItemImagePath.of(null);
        assertThat(path.toString()).isEmpty();
    }

    @Test
    @DisplayName("toString should return the value when present")
    void toStringShouldReturnValue() {
        var path = MenuItemImagePath.of("/images/pizza.jpg");
        assertThat(path.toString()).isEqualTo("/images/pizza.jpg");
    }
}

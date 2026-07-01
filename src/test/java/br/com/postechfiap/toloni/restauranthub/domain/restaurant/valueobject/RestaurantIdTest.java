package br.com.postechfiap.toloni.restauranthub.domain.restaurant.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class RestaurantIdTest {

    @Test
    @DisplayName("should create RestaurantId from UUID")
    void shouldCreateFromUUID() {
        var uuid = UUID.randomUUID();
        var id = RestaurantId.of(uuid);
        assertThat(id.getValue()).isEqualTo(uuid);
    }

    @Test
    @DisplayName("should create RestaurantId from UUID string")
    void shouldCreateFromString() {
        var uuid = UUID.randomUUID();
        var id = RestaurantId.of(uuid.toString());
        assertThat(id.getValue()).isEqualTo(uuid);
    }

    @Test
    @DisplayName("should throw when UUID string is invalid")
    void shouldThrowWhenInvalidString() {
        assertThatThrownBy(() -> RestaurantId.of("not-a-uuid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid RestaurantId: not-a-uuid");
    }

    @Test
    @DisplayName("should throw when UUID is null")
    void shouldThrowWhenNullUUID() {
        assertThatThrownBy(() -> RestaurantId.of((UUID) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("generate should produce unique IDs")
    void generateShouldProduceUniqueIds() {
        var id1 = RestaurantId.generate();
        var id2 = RestaurantId.generate();
        assertThat(id1).isNotEqualTo(id2);
    }

    @Test
    @DisplayName("should be equal when same UUID")
    void shouldBeEqualWhenSameUUID() {
        var uuid = UUID.randomUUID();
        var a = RestaurantId.of(uuid);
        var b = RestaurantId.of(uuid);
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("should not be equal when different UUID")
    void shouldNotBeEqualWhenDifferentUUID() {
        var a = RestaurantId.generate();
        var b = RestaurantId.generate();
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    @DisplayName("toString should return UUID string")
    void toStringShouldReturnUUIDString() {
        var uuid = UUID.randomUUID();
        var id = RestaurantId.of(uuid);
        assertThat(id.toString()).isEqualTo(uuid.toString());
    }
}

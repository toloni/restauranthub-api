package br.com.postechfiap.toloni.restauranthub.domain.user.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class UserIdTest {

    @Test
    @DisplayName("should create UserId from UUID")
    void shouldCreateFromUUID() {
        var uuid = UUID.randomUUID();
        var id = UserId.of(uuid);
        assertThat(id.getValue()).isEqualTo(uuid);
    }

    @Test
    @DisplayName("should create UserId from UUID string")
    void shouldCreateFromString() {
        var uuid = UUID.randomUUID();
        var id = UserId.of(uuid.toString());
        assertThat(id.getValue()).isEqualTo(uuid);
    }

    @Test
    @DisplayName("should throw when UUID string is invalid")
    void shouldThrowWhenInvalidString() {
        assertThatThrownBy(() -> UserId.of("invalid-uuid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid UserId: invalid-uuid");
    }

    @Test
    @DisplayName("should throw when UUID is null")
    void shouldThrowWhenNullUUID() {
        assertThatThrownBy(() -> UserId.of((UUID) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("generate should produce unique IDs")
    void generateShouldProduceUniqueIds() {
        var id1 = UserId.generate();
        var id2 = UserId.generate();
        assertThat(id1).isNotEqualTo(id2);
    }

    @Test
    @DisplayName("should be equal when same UUID")
    void shouldBeEqualWhenSameUUID() {
        var uuid = UUID.randomUUID();
        var a = UserId.of(uuid);
        var b = UserId.of(uuid);
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("should not be equal when different UUID")
    void shouldNotBeEqualWhenDifferentUUID() {
        var a = UserId.generate();
        var b = UserId.generate();
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    @DisplayName("toString should return UUID string")
    void toStringShouldReturnUUIDString() {
        var uuid = UUID.randomUUID();
        var id = UserId.of(uuid);
        assertThat(id.toString()).isEqualTo(uuid.toString());
    }
}

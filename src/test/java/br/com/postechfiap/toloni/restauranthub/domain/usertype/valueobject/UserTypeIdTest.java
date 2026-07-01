package br.com.postechfiap.toloni.restauranthub.domain.usertype.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class UserTypeIdTest {

    @Test
    @DisplayName("should create UserTypeId from UUID")
    void shouldCreateFromUUID() {
        var uuid = UUID.randomUUID();
        var id = UserTypeId.of(uuid);
        assertThat(id.getValue()).isEqualTo(uuid);
    }

    @Test
    @DisplayName("should create UserTypeId from UUID string")
    void shouldCreateFromString() {
        var uuid = UUID.randomUUID();
        var id = UserTypeId.of(uuid.toString());
        assertThat(id.getValue()).isEqualTo(uuid);
    }

    @Test
    @DisplayName("should throw when UUID string is invalid")
    void shouldThrowWhenInvalidString() {
        assertThatThrownBy(() -> UserTypeId.of("invalid-uuid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid UserTypeId: invalid-uuid");
    }

    @Test
    @DisplayName("should throw when UUID is null")
    void shouldThrowWhenNullUUID() {
        assertThatThrownBy(() -> UserTypeId.of((UUID) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("generate should produce unique IDs")
    void generateShouldProduceUniqueIds() {
        var id1 = UserTypeId.generate();
        var id2 = UserTypeId.generate();
        assertThat(id1).isNotEqualTo(id2);
    }

    @Test
    @DisplayName("should be equal when same UUID")
    void shouldBeEqualWhenSameUUID() {
        var uuid = UUID.randomUUID();
        var a = UserTypeId.of(uuid);
        var b = UserTypeId.of(uuid);
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("should not be equal when different UUID")
    void shouldNotBeEqualWhenDifferentUUID() {
        var a = UserTypeId.generate();
        var b = UserTypeId.generate();
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    @DisplayName("toString should return UUID string")
    void toStringShouldReturnUUIDString() {
        var uuid = UUID.randomUUID();
        var id = UserTypeId.of(uuid);
        assertThat(id.toString()).isEqualTo(uuid.toString());
    }
}

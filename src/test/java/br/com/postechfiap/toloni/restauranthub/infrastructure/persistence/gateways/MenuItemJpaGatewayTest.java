package br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.gateways;

import br.com.postechfiap.toloni.restauranthub.application.pagination.PageFilter;
import br.com.postechfiap.toloni.restauranthub.application.pagination.PageRequest;
import br.com.postechfiap.toloni.restauranthub.domain.menuitem.MenuItem;
import br.com.postechfiap.toloni.restauranthub.domain.menuitem.valueobject.*;
import br.com.postechfiap.toloni.restauranthub.domain.restaurant.Restaurant;
import br.com.postechfiap.toloni.restauranthub.domain.restaurant.valueobject.*;
import br.com.postechfiap.toloni.restauranthub.domain.user.User;
import br.com.postechfiap.toloni.restauranthub.domain.user.valueobject.UserEmail;
import br.com.postechfiap.toloni.restauranthub.domain.user.valueobject.UserId;
import br.com.postechfiap.toloni.restauranthub.domain.user.valueobject.UserName;
import br.com.postechfiap.toloni.restauranthub.domain.user.valueobject.UserPassword;
import br.com.postechfiap.toloni.restauranthub.domain.usertype.valueobject.UserTypeId;
import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.entities.MenuItemJpaEntity;
import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.entities.RestaurantJpaEntity;
import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.entities.UserJpaEntity;
import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.repositories.MenuItemJpaRepository;
import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.repositories.RestaurantJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class MenuItemJpaGatewayTest {

    @Mock
    private MenuItemJpaRepository jpaRepository;

    @Mock
    private RestaurantJpaRepository restaurantJpaRepository;

    @InjectMocks
    private MenuItemJpaGateway gateway;

    private MenuItemId id;
    private RestaurantId restaurantId;
    private MenuItem menuItem;
    private MenuItemJpaEntity menuItemEntity;
    private RestaurantJpaEntity restaurantEntity;

    @BeforeEach
    void setUp() {
        id = MenuItemId.generate();
        restaurantId = RestaurantId.generate();
        var ownerId = UserId.generate();
        var userTypeId = UserTypeId.generate();
        var owner = new User(
                ownerId,
                UserName.of("John Doe"),
                UserEmail.of("john@example.com"),
                UserPassword.of("password123"),
                userTypeId
        );
        var ownerEntity = UserJpaEntity.fromDomain(owner);

        var restaurant = new Restaurant(
                restaurantId,
                RestaurantName.of("The Great Burger"),
                RestaurantAddress.of("123 Main St, Springfield"),
                RestaurantCuisineType.of("American"),
                RestaurantOpeningHours.of("Mon-Fri 9am-10pm"),
                ownerId
        );
        restaurantEntity = RestaurantJpaEntity.fromDomain(restaurant, ownerEntity);

        menuItem = new MenuItem(
                id,
                MenuItemName.of("Classic Burger"),
                MenuItemDescription.of("Juicy beef patty with lettuce and tomato"),
                MenuItemPrice.ofBRL(new BigDecimal("19.90")),
                false,
                MenuItemImagePath.of("/images/classic-burger.jpg"),
                restaurantId
        );
        menuItemEntity = MenuItemJpaEntity.fromDomain(menuItem, restaurantEntity);
    }

    // -------------------------------------------------------------------------
    // save
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should save MenuItem successfully")
    void shouldSaveMenuItemSuccessfully() {
        when(restaurantJpaRepository.getReferenceById(restaurantId.getValue())).thenReturn(restaurantEntity);
        when(jpaRepository.save(any(MenuItemJpaEntity.class))).thenReturn(menuItemEntity);

        var result = gateway.save(menuItem);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(menuItem.getId());
        assertThat(result.getName()).isEqualTo(menuItem.getName());
        assertThat(result.getDescription()).isEqualTo(menuItem.getDescription());
        assertThat(result.getPrice()).isEqualTo(menuItem.getPrice());
        assertThat(result.isDineInOnly()).isEqualTo(menuItem.isDineInOnly());
        assertThat(result.getRestaurantId()).isEqualTo(menuItem.getRestaurantId());
    }

    @Test
    @DisplayName("Should call jpaRepository save once")
    void shouldCallJpaRepositorySaveOnce() {
        when(restaurantJpaRepository.getReferenceById(restaurantId.getValue())).thenReturn(restaurantEntity);
        when(jpaRepository.save(any(MenuItemJpaEntity.class))).thenReturn(menuItemEntity);

        gateway.save(menuItem);

        verify(jpaRepository, times(1)).save(any(MenuItemJpaEntity.class));
    }

    @Test
    @DisplayName("Should call restaurantJpaRepository getReferenceById with correct UUID")
    void shouldCallRestaurantJpaRepositoryGetReferenceByIdWithCorrectUUID() {
        when(restaurantJpaRepository.getReferenceById(restaurantId.getValue())).thenReturn(restaurantEntity);
        when(jpaRepository.save(any(MenuItemJpaEntity.class))).thenReturn(menuItemEntity);

        gateway.save(menuItem);

        verify(restaurantJpaRepository, times(1)).getReferenceById(restaurantId.getValue());
    }

    // -------------------------------------------------------------------------
    // findById
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should find MenuItem by id successfully")
    void shouldFindMenuItemByIdSuccessfully() {
        when(jpaRepository.findById(id.getValue())).thenReturn(Optional.of(menuItemEntity));

        var result = gateway.findById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getName().getValue()).isEqualTo("Classic Burger");
    }

    @Test
    @DisplayName("Should return empty when MenuItem is not found by id")
    void shouldReturnEmptyWhenMenuItemIsNotFoundById() {
        when(jpaRepository.findById(id.getValue())).thenReturn(Optional.empty());

        var result = gateway.findById(id);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should call jpaRepository findById with correct UUID")
    void shouldCallJpaRepositoryFindByIdWithCorrectUUID() {
        when(jpaRepository.findById(id.getValue())).thenReturn(Optional.of(menuItemEntity));

        gateway.findById(id);

        verify(jpaRepository, times(1)).findById(id.getValue());
    }

    // -------------------------------------------------------------------------
    // findAll
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should return paginated list of MenuItems")
    void shouldReturnPaginatedListOfMenuItems() {
        var pageRequest = PageRequest.of(0, 10);
        var springPage = new org.springframework.data.domain.PageImpl<>(
                List.of(menuItemEntity),
                org.springframework.data.domain.PageRequest.of(0, 10),
                1L
        );

        when(jpaRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(springPage);

        var result = gateway.findAll(pageRequest);

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(1);
        assertThat(result.totalElements()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should return empty page when no MenuItems exist")
    void shouldReturnEmptyPageWhenNoMenuItemsExist() {
        var pageRequest = PageRequest.of(0, 10);
        var springPage = new org.springframework.data.domain.PageImpl<MenuItemJpaEntity>(
                List.of(),
                org.springframework.data.domain.PageRequest.of(0, 10),
                0L
        );

        when(jpaRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(springPage);

        var result = gateway.findAll(pageRequest);

        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isZero();
    }

    // -------------------------------------------------------------------------
    // findAllByRestaurantId
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should return paginated list of MenuItems by restaurantId")
    void shouldReturnPaginatedListOfMenuItemsByRestaurantId() {
        var pageRequest = PageRequest.of(0, 10);
        var springPage = new org.springframework.data.domain.PageImpl<>(
                List.of(menuItemEntity),
                org.springframework.data.domain.PageRequest.of(0, 10),
                1L
        );

        when(jpaRepository.findAllByRestaurantId(eq(restaurantId.getValue()), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(springPage);

        var result = gateway.findAllByRestaurantId(restaurantId, pageRequest);

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().getFirst().getRestaurantId()).isEqualTo(restaurantId);
    }

    @Test
    @DisplayName("Should call jpaRepository findAllByRestaurantId with correct UUID")
    void shouldCallJpaRepositoryFindAllByRestaurantIdWithCorrectUUID() {
        var pageRequest = PageRequest.of(0, 10);
        var springPage = new org.springframework.data.domain.PageImpl<MenuItemJpaEntity>(
                List.of(),
                org.springframework.data.domain.PageRequest.of(0, 10),
                0L
        );

        when(jpaRepository.findAllByRestaurantId(eq(restaurantId.getValue()), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(springPage);

        gateway.findAllByRestaurantId(restaurantId, pageRequest);

        verify(jpaRepository, times(1)).findAllByRestaurantId(eq(restaurantId.getValue()), any(org.springframework.data.domain.Pageable.class));
    }

    // -------------------------------------------------------------------------
    // deleteById
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should delete MenuItem by id successfully")
    void shouldDeleteMenuItemByIdSuccessfully() {
        doNothing().when(jpaRepository).deleteById(id.getValue());

        assertThatNoException().isThrownBy(() -> gateway.deleteById(id));

        verify(jpaRepository, times(1)).deleteById(id.getValue());
    }

    // -------------------------------------------------------------------------
    // deleteAllByRestaurantId
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should delete all MenuItems by restaurantId successfully")
    void shouldDeleteAllMenuItemsByRestaurantIdSuccessfully() {
        doNothing().when(jpaRepository).deleteAllByRestaurantId(restaurantId.getValue());

        assertThatNoException().isThrownBy(() -> gateway.deleteAllByRestaurantId(restaurantId));

        verify(jpaRepository, times(1)).deleteAllByRestaurantId(restaurantId.getValue());
    }

    // -------------------------------------------------------------------------
    // existsByNameAndRestaurantId
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should return true when MenuItem with name exists in Restaurant")
    void shouldReturnTrueWhenMenuItemWithNameExistsInRestaurant() {
        when(jpaRepository.existsByNameAndRestaurantId("Classic Burger", restaurantId.getValue())).thenReturn(true);

        assertThat(gateway.existsByNameAndRestaurantId(MenuItemName.of("Classic Burger"), restaurantId)).isTrue();
    }

    @Test
    @DisplayName("Should return false when MenuItem with name does not exist in Restaurant")
    void shouldReturnFalseWhenMenuItemWithNameDoesNotExistInRestaurant() {
        when(jpaRepository.existsByNameAndRestaurantId("Classic Burger", restaurantId.getValue())).thenReturn(false);

        assertThat(gateway.existsByNameAndRestaurantId(MenuItemName.of("Classic Burger"), restaurantId)).isFalse();
    }

    @Test
    @DisplayName("Should call jpaRepository existsByNameAndRestaurantId with correct name and UUID")
    void shouldCallJpaRepositoryExistsByNameAndRestaurantIdWithCorrectNameAndUUID() {
        when(jpaRepository.existsByNameAndRestaurantId("Classic Burger", restaurantId.getValue())).thenReturn(true);

        gateway.existsByNameAndRestaurantId(MenuItemName.of("Classic Burger"), restaurantId);

        verify(jpaRepository, times(1)).existsByNameAndRestaurantId("Classic Burger", restaurantId.getValue());
    }

    // -------------------------------------------------------------------------
    // existsByNameAndRestaurantIdAndIdNot
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should return true when another MenuItem with name exists in Restaurant")
    void shouldReturnTrueWhenAnotherMenuItemWithNameExistsInRestaurant() {
        when(jpaRepository.existsByNameAndRestaurantIdAndIdNot("Classic Burger", restaurantId.getValue(), id.getValue()))
                .thenReturn(true);

        assertThat(gateway.existsByNameAndRestaurantIdAndIdNot(MenuItemName.of("Classic Burger"), restaurantId, id)).isTrue();
    }

    @Test
    @DisplayName("Should return false when no other MenuItem with name exists in Restaurant")
    void shouldReturnFalseWhenNoOtherMenuItemWithNameExistsInRestaurant() {
        when(jpaRepository.existsByNameAndRestaurantIdAndIdNot("Classic Burger", restaurantId.getValue(), id.getValue()))
                .thenReturn(false);

        assertThat(gateway.existsByNameAndRestaurantIdAndIdNot(MenuItemName.of("Classic Burger"), restaurantId, id)).isFalse();
    }

    @Test
    @DisplayName("Should call jpaRepository existsByNameAndRestaurantIdAndIdNot with correct parameters")
    void shouldCallJpaRepositoryExistsByNameAndRestaurantIdAndIdNotWithCorrectParameters() {
        when(jpaRepository.existsByNameAndRestaurantIdAndIdNot("Classic Burger", restaurantId.getValue(), id.getValue()))
                .thenReturn(false);

        gateway.existsByNameAndRestaurantIdAndIdNot(MenuItemName.of("Classic Burger"), restaurantId, id);

        verify(jpaRepository, times(1))
                .existsByNameAndRestaurantIdAndIdNot("Classic Burger", restaurantId.getValue(), id.getValue());
    }

    // -------------------------------------------------------------------------
    // existsByRestaurantId
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should return true when MenuItem exists for Restaurant")
    void shouldReturnTrueWhenMenuItemExistsForRestaurant() {
        when(jpaRepository.existsByRestaurantId(restaurantId.getValue())).thenReturn(true);

        assertThat(gateway.existsByRestaurantId(restaurantId)).isTrue();
    }

    @Test
    @DisplayName("Should return false when no MenuItem exists for Restaurant")
    void shouldReturnFalseWhenNoMenuItemExistsForRestaurant() {
        when(jpaRepository.existsByRestaurantId(restaurantId.getValue())).thenReturn(false);

        assertThat(gateway.existsByRestaurantId(restaurantId)).isFalse();
    }

    // -------------------------------------------------------------------------
    // findByIdWithRestaurantName
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should find MenuItem by id with restaurant name successfully")
    void shouldFindMenuItemByIdWithRestaurantNameSuccessfully() {
        when(jpaRepository.findById(id.getValue())).thenReturn(Optional.of(menuItemEntity));

        var result = gateway.findByIdWithRestaurantName(id);

        assertThat(result).isPresent();
        assertThat(result.get().menuItem().getId()).isEqualTo(id);
        assertThat(result.get().restaurantName()).isEqualTo("The Great Burger");
    }

    @Test
    @DisplayName("Should return empty when MenuItem is not found by id with restaurant name")
    void shouldReturnEmptyWhenMenuItemIsNotFoundByIdWithRestaurantName() {
        when(jpaRepository.findById(id.getValue())).thenReturn(Optional.empty());

        var result = gateway.findByIdWithRestaurantName(id);

        assertThat(result).isEmpty();
    }

    // -------------------------------------------------------------------------
    // findAllWithRestaurantName
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should return paginated list filtered by restaurantId when provided")
    void shouldReturnPaginatedListFilteredByRestaurantIdWhenProvided() {
        var pageRequest = PageRequest.of(0, 10);
        var springPage = new org.springframework.data.domain.PageImpl<>(
                List.of(menuItemEntity),
                org.springframework.data.domain.PageRequest.of(0, 10),
                1L
        );

        when(jpaRepository.findAllByRestaurantId(eq(restaurantId.getValue()), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(springPage);

        var result = gateway.findAllWithRestaurantName(restaurantId, pageRequest);

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().getFirst().restaurantName()).isEqualTo("The Great Burger");
        verify(jpaRepository, times(1)).findAllByRestaurantId(eq(restaurantId.getValue()), any(org.springframework.data.domain.Pageable.class));
    }

    @Test
    @DisplayName("Should return paginated list with filters when restaurantId is null")
    void shouldReturnPaginatedListWithFiltersWhenRestaurantIdIsNull() {
        var pageRequest = PageRequest.of(0, 10,
                List.of(PageFilter.of("name", "Burger")),
                List.of());
        var springPage = new org.springframework.data.domain.PageImpl<>(
                List.of(menuItemEntity),
                org.springframework.data.domain.PageRequest.of(0, 10),
                1L
        );

        when(jpaRepository.findAll(
                any(org.springframework.data.jpa.domain.Specification.class),
                any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(springPage);

        var result = gateway.findAllWithRestaurantName(null, pageRequest);

        assertThat(result.content()).hasSize(1);
        verify(jpaRepository, never()).findAllByRestaurantId(any(), any());
    }

    @Test
    @DisplayName("Should return empty page when no MenuItems exist with restaurant name")
    void shouldReturnEmptyPageWhenNoMenuItemsExistWithRestaurantName() {
        var pageRequest = PageRequest.of(0, 10);
        var springPage = new org.springframework.data.domain.PageImpl<MenuItemJpaEntity>(
                List.of(),
                org.springframework.data.domain.PageRequest.of(0, 10),
                0L
        );

        when(jpaRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(springPage);

        var result = gateway.findAllWithRestaurantName(null, pageRequest);

        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isZero();
    }
}
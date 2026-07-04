package br.com.postechfiap.toloni.restauranthub.infrastructure.web.rest.menuitem;

import br.com.postechfiap.toloni.restauranthub.adapters.controllers.MenuItemController;
import br.com.postechfiap.toloni.restauranthub.adapters.presenters.menuitem.MenuItemViewModel;
import br.com.postechfiap.toloni.restauranthub.application.pagination.Page;
import br.com.postechfiap.toloni.restauranthub.application.pagination.SortDirection;
import br.com.postechfiap.toloni.restauranthub.application.usecases.menuitem.*;
import br.com.postechfiap.toloni.restauranthub.domain.menuitem.valueobject.MenuItemId;
import br.com.postechfiap.toloni.restauranthub.domain.restaurant.valueobject.RestaurantId;
import br.com.postechfiap.toloni.restauranthub.domain.user.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class MenuItemRestControllerTest {

    @Mock
    private MenuItemController menuItemController;

    @InjectMocks
    private MenuItemRestController restController;

    private MenuItemId id;
    private RestaurantId restaurantId;
    private UserId ownerId;

    @BeforeEach
    void setUp() {
        id = MenuItemId.generate();
        restaurantId = RestaurantId.generate();
        ownerId = UserId.generate();
    }

    // -------------------------------------------------------------------------
    // create
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should create MenuItem and return response")
    void shouldCreateMenuItemAndReturnResponse() {
        var request = new MenuItemRequest("Classic Burger", "Juicy beef patty", new BigDecimal("19.90"),
                "BRL", false, "/images/classic-burger.jpg");
        var viewModel = new MenuItemViewModel(id.getValue(), "Classic Burger", "Juicy beef patty",
                new BigDecimal("19.90"), Currency.getInstance("BRL"), false,
                "/images/classic-burger.jpg", restaurantId.getValue(), null);

        when(menuItemController.create(any(CreateMenuItemUseCase.Input.class))).thenReturn(viewModel);

        var response = restController.create(restaurantId.getValue().toString(), ownerId.getValue().toString(), request);

        assertThat(response.id()).isEqualTo(id.getValue());
        assertThat(response.name()).isEqualTo("Classic Burger");
        assertThat(response.currency()).isEqualTo("BRL");
    }

    @Test
    @DisplayName("Should call menuItemController create with mapped input including restaurantId from path and ownerId from header")
    void shouldCallMenuItemControllerCreateWithMappedInputIncludingRestaurantIdFromPathAndOwnerIdFromHeader() {
        var request = new MenuItemRequest("Classic Burger", "Juicy beef patty", new BigDecimal("19.90"),
                "BRL", false, "/images/classic-burger.jpg");
        var viewModel = new MenuItemViewModel(id.getValue(), "Classic Burger", "Juicy beef patty",
                new BigDecimal("19.90"), Currency.getInstance("BRL"), false,
                "/images/classic-burger.jpg", restaurantId.getValue(), null);

        when(menuItemController.create(any(CreateMenuItemUseCase.Input.class))).thenReturn(viewModel);

        restController.create(restaurantId.getValue().toString(), ownerId.getValue().toString(), request);

        var captor = ArgumentCaptor.forClass(CreateMenuItemUseCase.Input.class);
        verify(menuItemController, times(1)).create(captor.capture());
        assertThat(captor.getValue().restaurantId()).isEqualTo(restaurantId);
        assertThat(captor.getValue().ownerId()).isEqualTo(ownerId);
        assertThat(captor.getValue().name()).isEqualTo("Classic Burger");
    }

    // -------------------------------------------------------------------------
    // update
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should update MenuItem and return response")
    void shouldUpdateMenuItemAndReturnResponse() {
        var request = new MenuItemRequest("Bacon Burger", null, null, null, null, null);
        var viewModel = new MenuItemViewModel(id.getValue(), "Bacon Burger", "Juicy beef patty",
                new BigDecimal("19.90"), Currency.getInstance("BRL"), false,
                "/images/classic-burger.jpg", restaurantId.getValue(), null);

        when(menuItemController.update(any(UpdateMenuItemUseCase.Input.class))).thenReturn(viewModel);

        var response = restController.update(id.getValue().toString(), ownerId.getValue().toString(), request);

        assertThat(response.id()).isEqualTo(id.getValue());
        assertThat(response.name()).isEqualTo("Bacon Burger");
    }

    @Test
    @DisplayName("Should call menuItemController update with mapped id, ownerId and input")
    void shouldCallMenuItemControllerUpdateWithMappedIdOwnerIdAndInput() {
        var request = new MenuItemRequest("Bacon Burger", null, null, null, null, null);
        var viewModel = new MenuItemViewModel(id.getValue(), "Bacon Burger", "Juicy beef patty",
                new BigDecimal("19.90"), Currency.getInstance("BRL"), false,
                "/images/classic-burger.jpg", restaurantId.getValue(), null);

        when(menuItemController.update(any(UpdateMenuItemUseCase.Input.class))).thenReturn(viewModel);

        restController.update(id.getValue().toString(), ownerId.getValue().toString(), request);

        var captor = ArgumentCaptor.forClass(UpdateMenuItemUseCase.Input.class);
        verify(menuItemController, times(1)).update(captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(id);
        assertThat(captor.getValue().ownerId()).isEqualTo(ownerId);
    }

    // -------------------------------------------------------------------------
    // delete
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should delete MenuItem")
    void shouldDeleteMenuItem() {
        doNothing().when(menuItemController).delete(any(DeleteMenuItemUseCase.Input.class));

        restController.delete(id.getValue().toString(), ownerId.getValue().toString());

        var captor = ArgumentCaptor.forClass(DeleteMenuItemUseCase.Input.class);
        verify(menuItemController, times(1)).delete(captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(id);
        assertThat(captor.getValue().ownerId()).isEqualTo(ownerId);
    }

    // -------------------------------------------------------------------------
    // findById
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should find MenuItem by id and return response")
    void shouldFindMenuItemByIdAndReturnResponse() {
        var viewModel = new MenuItemViewModel(id.getValue(), "Classic Burger", "Juicy beef patty",
                new BigDecimal("19.90"), Currency.getInstance("BRL"), false,
                "/images/classic-burger.jpg", restaurantId.getValue(), "The Great Burger");

        when(menuItemController.findById(any(FindMenuItemByIdUseCase.Input.class))).thenReturn(viewModel);

        var response = restController.findById(id.getValue().toString());

        assertThat(response.id()).isEqualTo(id.getValue());
        assertThat(response.restaurantName()).isEqualTo("The Great Burger");
    }

    // -------------------------------------------------------------------------
    // findAll
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should return paginated MenuItems with default parameters")
    void shouldReturnPaginatedMenuItemsWithDefaultParameters() {
        var viewModel = new MenuItemViewModel(id.getValue(), "Classic Burger", "Juicy beef patty",
                new BigDecimal("19.90"), Currency.getInstance("BRL"), false,
                "/images/classic-burger.jpg", restaurantId.getValue(), "The Great Burger");
        var page = Page.of(List.of(viewModel), 0, 10, 1L);

        when(menuItemController.findAll(any(FindAllMenuItemsUseCase.Input.class))).thenReturn(page);

        var response = restController.findAll(0, 10, null, null, null, null);

        assertThat(response.content()).hasSize(1);
        assertThat(response.content().getFirst().name()).isEqualTo("Classic Burger");
    }

    @Test
    @DisplayName("Should build Input with null restaurantId on global listing")
    void shouldBuildInputWithNullRestaurantIdOnGlobalListing() {
        var page = Page.<MenuItemViewModel>of(List.of(), 0, 10, 0L);

        when(menuItemController.findAll(any(FindAllMenuItemsUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, null, null, null, null);

        var captor = ArgumentCaptor.forClass(FindAllMenuItemsUseCase.Input.class);
        verify(menuItemController, times(1)).findAll(captor.capture());
        assertThat(captor.getValue().restaurantId()).isNull();
    }

    @Test
    @DisplayName("Should build Input with resolved restaurantId on restaurant-scoped listing")
    void shouldBuildInputWithResolvedRestaurantIdOnRestaurantScopedListing() {
        var page = Page.<MenuItemViewModel>of(List.of(), 0, 10, 0L);

        when(menuItemController.findAll(any(FindAllMenuItemsUseCase.Input.class))).thenReturn(page);

        restController.findAllByRestaurant(restaurantId.getValue().toString(), 0, 10, null, null, null, null);

        var captor = ArgumentCaptor.forClass(FindAllMenuItemsUseCase.Input.class);
        verify(menuItemController, times(1)).findAll(captor.capture());
        assertThat(captor.getValue().restaurantId()).isEqualTo(restaurantId);
    }

    @Test
    @DisplayName("Should build PageRequest without sorts when sort param is null")
    void shouldBuildPageRequestWithoutSortsWhenSortParamIsNull() {
        var page = Page.<MenuItemViewModel>of(List.of(), 0, 10, 0L);

        when(menuItemController.findAll(any(FindAllMenuItemsUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, null, null, null, null);

        var captor = ArgumentCaptor.forClass(FindAllMenuItemsUseCase.Input.class);
        verify(menuItemController, times(1)).findAll(captor.capture());
        assertThat(captor.getValue().pageRequest().hasSorts()).isFalse();
    }

    @Test
    @DisplayName("Should build PageRequest with ASC sort when direction is null")
    void shouldBuildPageRequestWithAscSortWhenDirectionIsNull() {
        var page = Page.<MenuItemViewModel>of(List.of(), 0, 10, 0L);

        when(menuItemController.findAll(any(FindAllMenuItemsUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, "name", null, null, null);

        var captor = ArgumentCaptor.forClass(FindAllMenuItemsUseCase.Input.class);
        verify(menuItemController, times(1)).findAll(captor.capture());
        var sorts = captor.getValue().pageRequest().sorts();
        assertThat(sorts.getFirst().direction()).isEqualTo(SortDirection.ASC);
    }

    @Test
    @DisplayName("Should build PageRequest with DESC sort when direction is provided")
    void shouldBuildPageRequestWithDescSortWhenDirectionIsProvided() {
        var page = Page.<MenuItemViewModel>of(List.of(), 0, 10, 0L);

        when(menuItemController.findAll(any(FindAllMenuItemsUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, "price", "DESC", null, null);

        var captor = ArgumentCaptor.forClass(FindAllMenuItemsUseCase.Input.class);
        verify(menuItemController, times(1)).findAll(captor.capture());
        var sorts = captor.getValue().pageRequest().sorts();
        assertThat(sorts.getFirst().direction()).isEqualTo(SortDirection.DESC);
    }

    @Test
    @DisplayName("Should build PageRequest with filter when filter and filterValue are provided")
    void shouldBuildPageRequestWithFilterWhenFilterAndFilterValueAreProvided() {
        var page = Page.<MenuItemViewModel>of(List.of(), 0, 10, 0L);

        when(menuItemController.findAll(any(FindAllMenuItemsUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, null, null, "name", "Burger");

        var captor = ArgumentCaptor.forClass(FindAllMenuItemsUseCase.Input.class);
        verify(menuItemController, times(1)).findAll(captor.capture());
        var filters = captor.getValue().pageRequest().filters();
        assertThat(filters.getFirst().field()).isEqualTo("name");
        assertThat(filters.getFirst().value()).isEqualTo("Burger");
    }

    @Test
    @DisplayName("Should return empty page when no MenuItems exist")
    void shouldReturnEmptyPageWhenNoMenuItemsExist() {
        var page = Page.<MenuItemViewModel>of(List.of(), 0, 10, 0L);

        when(menuItemController.findAll(any(FindAllMenuItemsUseCase.Input.class))).thenReturn(page);

        var response = restController.findAll(0, 10, null, null, null, null);

        assertThat(response.content()).isEmpty();
        assertThat(response.totalElements()).isZero();
    }
}

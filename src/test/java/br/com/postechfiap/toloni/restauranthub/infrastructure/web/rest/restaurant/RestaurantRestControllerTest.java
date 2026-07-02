package br.com.postechfiap.toloni.restauranthub.infrastructure.web.rest.restaurant;

import br.com.postechfiap.toloni.restauranthub.adapters.controllers.RestaurantController;
import br.com.postechfiap.toloni.restauranthub.adapters.presenters.restaurant.RestaurantViewModel;
import br.com.postechfiap.toloni.restauranthub.adapters.presenters.restaurant.TransferOwnershipViewModel;
import br.com.postechfiap.toloni.restauranthub.application.pagination.Page;
import br.com.postechfiap.toloni.restauranthub.application.pagination.SortDirection;
import br.com.postechfiap.toloni.restauranthub.application.usecases.restaurant.*;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class RestaurantRestControllerTest {

    @Mock
    private RestaurantController restaurantController;

    @InjectMocks
    private RestaurantRestController restController;

    private RestaurantId id;
    private UserId ownerId;
    private UserId requesterId;
    private UserId newOwnerId;

    @BeforeEach
    void setUp() {
        id = RestaurantId.generate();
        ownerId = UserId.generate();
        requesterId = UserId.generate();
        newOwnerId = UserId.generate();
    }

    // -------------------------------------------------------------------------
    // create
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should create Restaurant and return response")
    void shouldCreateRestaurantAndReturnResponse() {
        var request = new RestaurantRequest("The Great Burger", "123 Main St, Springfield", "American", "Mon-Fri 9am-10pm");
        var viewModel = new RestaurantViewModel(
                id.getValue(), "The Great Burger", "123 Main St, Springfield",
                "American", "Mon-Fri 9am-10pm", ownerId.getValue(), null
        );

        when(restaurantController.create(any(CreateRestaurantUseCase.Input.class))).thenReturn(viewModel);

        var response = restController.create(ownerId.getValue().toString(), request);

        assertThat(response.id()).isEqualTo(id.getValue());
        assertThat(response.name()).isEqualTo("The Great Burger");
        assertThat(response.ownerId()).isEqualTo(ownerId.getValue());
    }

    @Test
    @DisplayName("Should call restaurantController create with mapped input including ownerId from header")
    void shouldCallRestaurantControllerCreateWithMappedInputIncludingOwnerIdFromHeader() {
        var request = new RestaurantRequest("The Great Burger", "123 Main St, Springfield", "American", "Mon-Fri 9am-10pm");
        var viewModel = new RestaurantViewModel(
                id.getValue(), "The Great Burger", "123 Main St, Springfield",
                "American", "Mon-Fri 9am-10pm", ownerId.getValue(), null
        );

        when(restaurantController.create(any(CreateRestaurantUseCase.Input.class))).thenReturn(viewModel);

        restController.create(ownerId.getValue().toString(), request);

        var captor = ArgumentCaptor.forClass(CreateRestaurantUseCase.Input.class);
        verify(restaurantController, times(1)).create(captor.capture());
        assertThat(captor.getValue().ownerId()).isEqualTo(ownerId);
        assertThat(captor.getValue().name()).isEqualTo("The Great Burger");
    }

    // -------------------------------------------------------------------------
    // update
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should update Restaurant and return response")
    void shouldUpdateRestaurantAndReturnResponse() {
        var request = new RestaurantRequest("New Burger", null, null, null);
        var viewModel = new RestaurantViewModel(
                id.getValue(), "New Burger", "123 Main St, Springfield",
                "American", "Mon-Fri 9am-10pm", ownerId.getValue(), null
        );

        when(restaurantController.update(any(UpdateRestaurantUseCase.Input.class))).thenReturn(viewModel);

        var response = restController.update(id.getValue().toString(), ownerId.getValue().toString(), request);

        assertThat(response.id()).isEqualTo(id.getValue());
        assertThat(response.name()).isEqualTo("New Burger");
    }

    @Test
    @DisplayName("Should call restaurantController update with mapped id, ownerId and input")
    void shouldCallRestaurantControllerUpdateWithMappedIdOwnerIdAndInput() {
        var request = new RestaurantRequest("New Burger", null, null, null);
        var viewModel = new RestaurantViewModel(
                id.getValue(), "New Burger", "123 Main St, Springfield",
                "American", "Mon-Fri 9am-10pm", ownerId.getValue(), null
        );

        when(restaurantController.update(any(UpdateRestaurantUseCase.Input.class))).thenReturn(viewModel);

        restController.update(id.getValue().toString(), ownerId.getValue().toString(), request);

        var captor = ArgumentCaptor.forClass(UpdateRestaurantUseCase.Input.class);
        verify(restaurantController, times(1)).update(captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(id);
        assertThat(captor.getValue().ownerId()).isEqualTo(ownerId);
    }

    // -------------------------------------------------------------------------
    // transferOwnership
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should transfer ownership and return response")
    void shouldTransferOwnershipAndReturnResponse() {
        var request = new RestaurantTransferOwnershipRequest(newOwnerId.getValue().toString());
        var viewModel = new TransferOwnershipViewModel(newOwnerId.getValue());

        when(restaurantController.transferOwnership(any(TransferRestaurantOwnershipUseCase.Input.class)))
                .thenReturn(viewModel);

        var response = restController.transferOwnership(id.getValue().toString(), requesterId.getValue().toString(), request);

        assertThat(response.newOwnerId()).isEqualTo(newOwnerId.getValue().toString());
    }

    @Test
    @DisplayName("Should call restaurantController transferOwnership with mapped ids")
    void shouldCallRestaurantControllerTransferOwnershipWithMappedIds() {
        var request = new RestaurantTransferOwnershipRequest(newOwnerId.getValue().toString());
        var viewModel = new TransferOwnershipViewModel(newOwnerId.getValue());

        when(restaurantController.transferOwnership(any(TransferRestaurantOwnershipUseCase.Input.class)))
                .thenReturn(viewModel);

        restController.transferOwnership(id.getValue().toString(), requesterId.getValue().toString(), request);

        var captor = ArgumentCaptor.forClass(TransferRestaurantOwnershipUseCase.Input.class);
        verify(restaurantController, times(1)).transferOwnership(captor.capture());
        assertThat(captor.getValue().restaurantId()).isEqualTo(id);
        assertThat(captor.getValue().requesterId()).isEqualTo(requesterId);
        assertThat(captor.getValue().newOwnerId()).isEqualTo(newOwnerId);
    }

    // -------------------------------------------------------------------------
    // delete
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should delete Restaurant")
    void shouldDeleteRestaurant() {
        doNothing().when(restaurantController).delete(any(DeleteRestaurantUseCase.Input.class));

        restController.delete(id.getValue().toString(), ownerId.getValue().toString());

        var captor = ArgumentCaptor.forClass(DeleteRestaurantUseCase.Input.class);
        verify(restaurantController, times(1)).delete(captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(id);
        assertThat(captor.getValue().ownerId()).isEqualTo(ownerId);
    }

    // -------------------------------------------------------------------------
    // findById
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should find Restaurant by id and return response")
    void shouldFindRestaurantByIdAndReturnResponse() {
        var viewModel = new RestaurantViewModel(
                id.getValue(), "The Great Burger", "123 Main St, Springfield",
                "American", "Mon-Fri 9am-10pm", ownerId.getValue(), "John Doe"
        );

        when(restaurantController.findById(any(FindRestaurantByIdUseCase.Input.class))).thenReturn(viewModel);

        var response = restController.findById(id.getValue().toString());

        assertThat(response.id()).isEqualTo(id.getValue());
        assertThat(response.ownerName()).isEqualTo("John Doe");
    }

    // -------------------------------------------------------------------------
    // findAll
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should return paginated Restaurants with default parameters")
    void shouldReturnPaginatedRestaurantsWithDefaultParameters() {
        var viewModel = new RestaurantViewModel(
                id.getValue(), "The Great Burger", "123 Main St, Springfield",
                "American", "Mon-Fri 9am-10pm", ownerId.getValue(), "John Doe"
        );
        var page = Page.of(List.of(viewModel), 0, 10, 1L);

        when(restaurantController.findAll(any(FindAllRestaurantsUseCase.Input.class))).thenReturn(page);

        var response = restController.findAll(0, 10, null, null, null, null);

        assertThat(response.content()).hasSize(1);
        assertThat(response.content().getFirst().name()).isEqualTo("The Great Burger");
    }

    @Test
    @DisplayName("Should build PageRequest without sorts when sort param is null")
    void shouldBuildPageRequestWithoutSortsWhenSortParamIsNull() {
        var page = Page.<RestaurantViewModel>of(List.of(), 0, 10, 0L);

        when(restaurantController.findAll(any(FindAllRestaurantsUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, null, null, null, null);

        var captor = ArgumentCaptor.forClass(FindAllRestaurantsUseCase.Input.class);
        verify(restaurantController, times(1)).findAll(captor.capture());
        assertThat(captor.getValue().pageRequest().hasSorts()).isFalse();
    }

    @Test
    @DisplayName("Should build PageRequest with ASC sort when direction is null")
    void shouldBuildPageRequestWithAscSortWhenDirectionIsNull() {
        var page = Page.<RestaurantViewModel>of(List.of(), 0, 10, 0L);

        when(restaurantController.findAll(any(FindAllRestaurantsUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, "name", null, null, null);

        var captor = ArgumentCaptor.forClass(FindAllRestaurantsUseCase.Input.class);
        verify(restaurantController, times(1)).findAll(captor.capture());
        var sorts = captor.getValue().pageRequest().sorts();
        assertThat(sorts.getFirst().direction()).isEqualTo(SortDirection.ASC);
    }

    @Test
    @DisplayName("Should build PageRequest with DESC sort when direction is provided")
    void shouldBuildPageRequestWithDescSortWhenDirectionIsProvided() {
        var page = Page.<RestaurantViewModel>of(List.of(), 0, 10, 0L);

        when(restaurantController.findAll(any(FindAllRestaurantsUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, "name", "DESC", null, null);

        var captor = ArgumentCaptor.forClass(FindAllRestaurantsUseCase.Input.class);
        verify(restaurantController, times(1)).findAll(captor.capture());
        var sorts = captor.getValue().pageRequest().sorts();
        assertThat(sorts.getFirst().direction()).isEqualTo(SortDirection.DESC);
    }

    @Test
    @DisplayName("Should build PageRequest with filter when filter and filterValue are provided")
    void shouldBuildPageRequestWithFilterWhenFilterAndFilterValueAreProvided() {
        var page = Page.<RestaurantViewModel>of(List.of(), 0, 10, 0L);

        when(restaurantController.findAll(any(FindAllRestaurantsUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, null, null, "cuisineType", "Italian");

        var captor = ArgumentCaptor.forClass(FindAllRestaurantsUseCase.Input.class);
        verify(restaurantController, times(1)).findAll(captor.capture());
        var filters = captor.getValue().pageRequest().filters();
        assertThat(filters.getFirst().field()).isEqualTo("cuisineType");
        assertThat(filters.getFirst().value()).isEqualTo("Italian");
    }

    @Test
    @DisplayName("Should return empty page when no Restaurants exist")
    void shouldReturnEmptyPageWhenNoRestaurantsExist() {
        var page = Page.<RestaurantViewModel>of(List.of(), 0, 10, 0L);

        when(restaurantController.findAll(any(FindAllRestaurantsUseCase.Input.class))).thenReturn(page);

        var response = restController.findAll(0, 10, null, null, null, null);

        assertThat(response.content()).isEmpty();
        assertThat(response.totalElements()).isZero();
    }
}

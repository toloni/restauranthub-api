package br.com.postechfiap.toloni.restauranthub.infrastructure.web.rest.usertype;

import br.com.postechfiap.toloni.restauranthub.adapters.controllers.UserTypeController;
import br.com.postechfiap.toloni.restauranthub.adapters.presenters.usertype.UserTypeViewModel;
import br.com.postechfiap.toloni.restauranthub.application.pagination.Page;
import br.com.postechfiap.toloni.restauranthub.application.pagination.SortDirection;
import br.com.postechfiap.toloni.restauranthub.application.usecases.usertype.*;
import br.com.postechfiap.toloni.restauranthub.domain.usertype.UserRole;
import br.com.postechfiap.toloni.restauranthub.domain.usertype.valueobject.UserTypeId;
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
class UserTypeRestControllerTest {

    @Mock
    private UserTypeController userTypeController;

    @InjectMocks
    private UserTypeRestController restController;

    private UserTypeId id;

    @BeforeEach
    void setUp() {
        id = UserTypeId.generate();
    }

    // -------------------------------------------------------------------------
    // create
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should create UserType and return response")
    void shouldCreateUserTypeAndReturnResponse() {
        var request = new UserTypeRequest("Restaurant Owner", "Owns and manages a restaurant", UserRole.RESTAURANT_OWNER);
        var viewModel = new UserTypeViewModel(id.getValue(), "Restaurant Owner", "Owns and manages a restaurant", UserRole.RESTAURANT_OWNER);

        when(userTypeController.create(any(CreateUserTypeUseCase.Input.class))).thenReturn(viewModel);

        var response = restController.create(request);

        assertThat(response.id()).isEqualTo(id.getValue());
        assertThat(response.name()).isEqualTo("Restaurant Owner");
        assertThat(response.description()).isEqualTo("Owns and manages a restaurant");
        assertThat(response.role()).isEqualTo(UserRole.RESTAURANT_OWNER);
    }

    @Test
    @DisplayName("Should call userTypeController create with mapped input")
    void shouldCallUserTypeControllerCreateWithMappedInput() {
        var request = new UserTypeRequest("Restaurant Owner", "Owns and manages a restaurant", UserRole.RESTAURANT_OWNER);
        var viewModel = new UserTypeViewModel(id.getValue(), "Restaurant Owner", "Owns and manages a restaurant", UserRole.RESTAURANT_OWNER);

        when(userTypeController.create(any(CreateUserTypeUseCase.Input.class))).thenReturn(viewModel);

        restController.create(request);

        var captor = ArgumentCaptor.forClass(CreateUserTypeUseCase.Input.class);
        verify(userTypeController, times(1)).create(captor.capture());
        assertThat(captor.getValue().name()).isEqualTo("Restaurant Owner");
        assertThat(captor.getValue().role()).isEqualTo(UserRole.RESTAURANT_OWNER);
    }

    // -------------------------------------------------------------------------
    // update
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should update UserType and return response")
    void shouldUpdateUserTypeAndReturnResponse() {
        var request = new UserTypeRequest("Updated Name", "Updated description", UserRole.CUSTOMER);
        var viewModel = new UserTypeViewModel(id.getValue(), "Updated Name", "Updated description", UserRole.CUSTOMER);

        when(userTypeController.update(any(UpdateUserTypeUseCase.Input.class))).thenReturn(viewModel);

        var response = restController.update(id.getValue().toString(), request);

        assertThat(response.id()).isEqualTo(id.getValue());
        assertThat(response.name()).isEqualTo("Updated Name");
        assertThat(response.role()).isEqualTo(UserRole.CUSTOMER);
    }

    @Test
    @DisplayName("Should call userTypeController update with mapped id and input")
    void shouldCallUserTypeControllerUpdateWithMappedIdAndInput() {
        var request = new UserTypeRequest("Updated Name", "Updated description", UserRole.CUSTOMER);
        var viewModel = new UserTypeViewModel(id.getValue(), "Updated Name", "Updated description", UserRole.CUSTOMER);

        when(userTypeController.update(any(UpdateUserTypeUseCase.Input.class))).thenReturn(viewModel);

        restController.update(id.getValue().toString(), request);

        var captor = ArgumentCaptor.forClass(UpdateUserTypeUseCase.Input.class);
        verify(userTypeController, times(1)).update(captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(id);
        assertThat(captor.getValue().name()).isEqualTo("Updated Name");
    }

    // -------------------------------------------------------------------------
    // delete
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should delete UserType")
    void shouldDeleteUserType() {
        doNothing().when(userTypeController).delete(any(DeleteUserTypeUseCase.Input.class));

        restController.delete(id.getValue().toString());

        var captor = ArgumentCaptor.forClass(DeleteUserTypeUseCase.Input.class);
        verify(userTypeController, times(1)).delete(captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(id);
    }

    // -------------------------------------------------------------------------
    // findById
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should find UserType by id and return response")
    void shouldFindUserTypeByIdAndReturnResponse() {
        var viewModel = new UserTypeViewModel(id.getValue(), "Restaurant Owner", "Owns and manages a restaurant", UserRole.RESTAURANT_OWNER);

        when(userTypeController.findById(any(FindUserTypeByIdUseCase.Input.class))).thenReturn(viewModel);

        var response = restController.findById(id.getValue().toString());

        assertThat(response.id()).isEqualTo(id.getValue());
        assertThat(response.name()).isEqualTo("Restaurant Owner");
    }

    @Test
    @DisplayName("Should call userTypeController findById with mapped id")
    void shouldCallUserTypeControllerFindByIdWithMappedId() {
        var viewModel = new UserTypeViewModel(id.getValue(), "Restaurant Owner", "Owns and manages a restaurant", UserRole.RESTAURANT_OWNER);

        when(userTypeController.findById(any(FindUserTypeByIdUseCase.Input.class))).thenReturn(viewModel);

        restController.findById(id.getValue().toString());

        var captor = ArgumentCaptor.forClass(FindUserTypeByIdUseCase.Input.class);
        verify(userTypeController, times(1)).findById(captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(id);
    }

    // -------------------------------------------------------------------------
    // findAll
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should return paginated UserTypes with default parameters")
    void shouldReturnPaginatedUserTypesWithDefaultParameters() {
        var viewModel = new UserTypeViewModel(id.getValue(), "Restaurant Owner", "Owns and manages a restaurant", UserRole.RESTAURANT_OWNER);
        var page = Page.of(List.of(viewModel), 0, 10, 1L);

        when(userTypeController.findAll(any(FindAllUserTypesUseCase.Input.class))).thenReturn(page);

        var response = restController.findAll(0, 10, null, null, null, null);

        assertThat(response.content()).hasSize(1);
        assertThat(response.content().getFirst().name()).isEqualTo("Restaurant Owner");
        assertThat(response.pageNumber()).isZero();
        assertThat(response.pageSize()).isEqualTo(10);
        assertThat(response.totalElements()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should build PageRequest without sorts when sort param is null")
    void shouldBuildPageRequestWithoutSortsWhenSortParamIsNull() {
        var page = Page.<UserTypeViewModel>of(List.of(), 0, 10, 0L);

        when(userTypeController.findAll(any(FindAllUserTypesUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, null, null, null, null);

        var captor = ArgumentCaptor.forClass(FindAllUserTypesUseCase.Input.class);
        verify(userTypeController, times(1)).findAll(captor.capture());
        assertThat(captor.getValue().pageRequest().hasSorts()).isFalse();
    }

    @Test
    @DisplayName("Should build PageRequest with ASC sort when direction is null")
    void shouldBuildPageRequestWithAscSortWhenDirectionIsNull() {
        var page = Page.<UserTypeViewModel>of(List.of(), 0, 10, 0L);

        when(userTypeController.findAll(any(FindAllUserTypesUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, "name", null, null, null);

        var captor = ArgumentCaptor.forClass(FindAllUserTypesUseCase.Input.class);
        verify(userTypeController, times(1)).findAll(captor.capture());
        var sorts = captor.getValue().pageRequest().sorts();
        assertThat(sorts).hasSize(1);
        assertThat(sorts.getFirst().field()).isEqualTo("name");
        assertThat(sorts.getFirst().direction()).isEqualTo(SortDirection.ASC);
    }

    @Test
    @DisplayName("Should build PageRequest with DESC sort when direction is provided")
    void shouldBuildPageRequestWithDescSortWhenDirectionIsProvided() {
        var page = Page.<UserTypeViewModel>of(List.of(), 0, 10, 0L);

        when(userTypeController.findAll(any(FindAllUserTypesUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, "name", "DESC", null, null);

        var captor = ArgumentCaptor.forClass(FindAllUserTypesUseCase.Input.class);
        verify(userTypeController, times(1)).findAll(captor.capture());
        var sorts = captor.getValue().pageRequest().sorts();
        assertThat(sorts.getFirst().direction()).isEqualTo(SortDirection.DESC);
    }

    @Test
    @DisplayName("Should build PageRequest without filters when filter param is null")
    void shouldBuildPageRequestWithoutFiltersWhenFilterParamIsNull() {
        var page = Page.<UserTypeViewModel>of(List.of(), 0, 10, 0L);

        when(userTypeController.findAll(any(FindAllUserTypesUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, null, null, null, null);

        var captor = ArgumentCaptor.forClass(FindAllUserTypesUseCase.Input.class);
        verify(userTypeController, times(1)).findAll(captor.capture());
        assertThat(captor.getValue().pageRequest().hasFilters()).isFalse();
    }

    @Test
    @DisplayName("Should build PageRequest without filters when filterValue is null")
    void shouldBuildPageRequestWithoutFiltersWhenFilterValueIsNull() {
        var page = Page.<UserTypeViewModel>of(List.of(), 0, 10, 0L);

        when(userTypeController.findAll(any(FindAllUserTypesUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, null, null, "name", null);

        var captor = ArgumentCaptor.forClass(FindAllUserTypesUseCase.Input.class);
        verify(userTypeController, times(1)).findAll(captor.capture());
        assertThat(captor.getValue().pageRequest().hasFilters()).isFalse();
    }

    @Test
    @DisplayName("Should build PageRequest with filter when filter and filterValue are provided")
    void shouldBuildPageRequestWithFilterWhenFilterAndFilterValueAreProvided() {
        var page = Page.<UserTypeViewModel>of(List.of(), 0, 10, 0L);

        when(userTypeController.findAll(any(FindAllUserTypesUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, null, null, "name", "Restaurant");

        var captor = ArgumentCaptor.forClass(FindAllUserTypesUseCase.Input.class);
        verify(userTypeController, times(1)).findAll(captor.capture());
        var filters = captor.getValue().pageRequest().filters();
        assertThat(filters).hasSize(1);
        assertThat(filters.getFirst().field()).isEqualTo("name");
        assertThat(filters.getFirst().value()).isEqualTo("Restaurant");
    }

    @Test
    @DisplayName("Should return empty page when no UserTypes exist")
    void shouldReturnEmptyPageWhenNoUserTypesExist() {
        var page = Page.<UserTypeViewModel>of(List.of(), 0, 10, 0L);

        when(userTypeController.findAll(any(FindAllUserTypesUseCase.Input.class))).thenReturn(page);

        var response = restController.findAll(0, 10, null, null, null, null);

        assertThat(response.content()).isEmpty();
        assertThat(response.totalElements()).isZero();
    }
}

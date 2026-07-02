package br.com.postechfiap.toloni.restauranthub.infrastructure.web.rest.user;

import br.com.postechfiap.toloni.restauranthub.adapters.controllers.UserController;
import br.com.postechfiap.toloni.restauranthub.adapters.presenters.user.UserViewModel;
import br.com.postechfiap.toloni.restauranthub.application.pagination.Page;
import br.com.postechfiap.toloni.restauranthub.application.pagination.SortDirection;
import br.com.postechfiap.toloni.restauranthub.application.usecases.user.*;
import br.com.postechfiap.toloni.restauranthub.domain.user.valueobject.UserId;
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
class UserRestControllerTest {

    @Mock
    private UserController userController;

    @InjectMocks
    private UserRestController restController;

    private UserId id;
    private UserTypeId userTypeId;

    @BeforeEach
    void setUp() {
        id = UserId.generate();
        userTypeId = UserTypeId.generate();
    }

    // -------------------------------------------------------------------------
    // create
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should create User and return response")
    void shouldCreateUserAndReturnResponse() {
        var request = new UserRequest("John Doe", "john@example.com", "password123", userTypeId.getValue().toString());
        var viewModel = new UserViewModel(id.getValue(), "John Doe", "john@example.com", userTypeId.getValue(), null);

        when(userController.create(any(CreateUserUseCase.Input.class))).thenReturn(viewModel);

        var response = restController.create(request);

        assertThat(response.id()).isEqualTo(id.getValue());
        assertThat(response.name()).isEqualTo("John Doe");
        assertThat(response.email()).isEqualTo("john@example.com");
        assertThat(response.userTypeId()).isEqualTo(userTypeId.getValue());
    }

    @Test
    @DisplayName("Should call userController create with mapped input")
    void shouldCallUserControllerCreateWithMappedInput() {
        var request = new UserRequest("John Doe", "john@example.com", "password123", userTypeId.getValue().toString());
        var viewModel = new UserViewModel(id.getValue(), "John Doe", "john@example.com", userTypeId.getValue(), null);

        when(userController.create(any(CreateUserUseCase.Input.class))).thenReturn(viewModel);

        restController.create(request);

        var captor = ArgumentCaptor.forClass(CreateUserUseCase.Input.class);
        verify(userController, times(1)).create(captor.capture());
        assertThat(captor.getValue().name()).isEqualTo("John Doe");
        assertThat(captor.getValue().email()).isEqualTo("john@example.com");
        assertThat(captor.getValue().userTypeId()).isEqualTo(userTypeId);
    }

    // -------------------------------------------------------------------------
    // update
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should update User and return response")
    void shouldUpdateUserAndReturnResponse() {
        var request = new UserRequest("Jane Doe", null, null, userTypeId.getValue().toString());
        var viewModel = new UserViewModel(id.getValue(), "Jane Doe", "john@example.com", userTypeId.getValue(), null);

        when(userController.update(any(UpdateUserUseCase.Input.class))).thenReturn(viewModel);

        var response = restController.update(id.getValue().toString(), request);

        assertThat(response.id()).isEqualTo(id.getValue());
        assertThat(response.name()).isEqualTo("Jane Doe");
    }

    @Test
    @DisplayName("Should call userController update with mapped id and input")
    void shouldCallUserControllerUpdateWithMappedIdAndInput() {
        var request = new UserRequest("Jane Doe", null, null, userTypeId.getValue().toString());
        var viewModel = new UserViewModel(id.getValue(), "Jane Doe", "john@example.com", userTypeId.getValue(), null);

        when(userController.update(any(UpdateUserUseCase.Input.class))).thenReturn(viewModel);

        restController.update(id.getValue().toString(), request);

        var captor = ArgumentCaptor.forClass(UpdateUserUseCase.Input.class);
        verify(userController, times(1)).update(captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(id);
        assertThat(captor.getValue().name()).isEqualTo("Jane Doe");
    }

    // -------------------------------------------------------------------------
    // delete
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should delete User")
    void shouldDeleteUser() {
        doNothing().when(userController).delete(any(DeleteUserUseCase.Input.class));

        restController.delete(id.getValue().toString());

        var captor = ArgumentCaptor.forClass(DeleteUserUseCase.Input.class);
        verify(userController, times(1)).delete(captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(id);
    }

    // -------------------------------------------------------------------------
    // findById
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should find User by id and return response")
    void shouldFindUserByIdAndReturnResponse() {
        var viewModel = new UserViewModel(id.getValue(), "John Doe", "john@example.com", userTypeId.getValue(), "Restaurant Owner");

        when(userController.findById(any(FindUserByIdUseCase.Input.class))).thenReturn(viewModel);

        var response = restController.findById(id.getValue().toString());

        assertThat(response.id()).isEqualTo(id.getValue());
        assertThat(response.userTypeName()).isEqualTo("Restaurant Owner");
    }

    @Test
    @DisplayName("Should call userController findById with mapped id")
    void shouldCallUserControllerFindByIdWithMappedId() {
        var viewModel = new UserViewModel(id.getValue(), "John Doe", "john@example.com", userTypeId.getValue(), "Restaurant Owner");

        when(userController.findById(any(FindUserByIdUseCase.Input.class))).thenReturn(viewModel);

        restController.findById(id.getValue().toString());

        var captor = ArgumentCaptor.forClass(FindUserByIdUseCase.Input.class);
        verify(userController, times(1)).findById(captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(id);
    }

    // -------------------------------------------------------------------------
    // findAll
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should return paginated Users with default parameters")
    void shouldReturnPaginatedUsersWithDefaultParameters() {
        var viewModel = new UserViewModel(id.getValue(), "John Doe", "john@example.com", userTypeId.getValue(), "Restaurant Owner");
        var page = Page.of(List.of(viewModel), 0, 10, 1L);

        when(userController.findAll(any(FindAllUsersUseCase.Input.class))).thenReturn(page);

        var response = restController.findAll(0, 10, null, null, null, null);

        assertThat(response.content()).hasSize(1);
        assertThat(response.content().getFirst().name()).isEqualTo("John Doe");
        assertThat(response.totalElements()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should build PageRequest without sorts when sort param is null")
    void shouldBuildPageRequestWithoutSortsWhenSortParamIsNull() {
        var page = Page.<UserViewModel>of(List.of(), 0, 10, 0L);

        when(userController.findAll(any(FindAllUsersUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, null, null, null, null);

        var captor = ArgumentCaptor.forClass(FindAllUsersUseCase.Input.class);
        verify(userController, times(1)).findAll(captor.capture());
        assertThat(captor.getValue().pageRequest().hasSorts()).isFalse();
    }

    @Test
    @DisplayName("Should build PageRequest with ASC sort when direction is null")
    void shouldBuildPageRequestWithAscSortWhenDirectionIsNull() {
        var page = Page.<UserViewModel>of(List.of(), 0, 10, 0L);

        when(userController.findAll(any(FindAllUsersUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, "name", null, null, null);

        var captor = ArgumentCaptor.forClass(FindAllUsersUseCase.Input.class);
        verify(userController, times(1)).findAll(captor.capture());
        var sorts = captor.getValue().pageRequest().sorts();
        assertThat(sorts.getFirst().direction()).isEqualTo(SortDirection.ASC);
    }

    @Test
    @DisplayName("Should build PageRequest with DESC sort when direction is provided")
    void shouldBuildPageRequestWithDescSortWhenDirectionIsProvided() {
        var page = Page.<UserViewModel>of(List.of(), 0, 10, 0L);

        when(userController.findAll(any(FindAllUsersUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, "name", "DESC", null, null);

        var captor = ArgumentCaptor.forClass(FindAllUsersUseCase.Input.class);
        verify(userController, times(1)).findAll(captor.capture());
        var sorts = captor.getValue().pageRequest().sorts();
        assertThat(sorts.getFirst().direction()).isEqualTo(SortDirection.DESC);
    }

    @Test
    @DisplayName("Should build PageRequest without filters when filter param is null")
    void shouldBuildPageRequestWithoutFiltersWhenFilterParamIsNull() {
        var page = Page.<UserViewModel>of(List.of(), 0, 10, 0L);

        when(userController.findAll(any(FindAllUsersUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, null, null, null, null);

        var captor = ArgumentCaptor.forClass(FindAllUsersUseCase.Input.class);
        verify(userController, times(1)).findAll(captor.capture());
        assertThat(captor.getValue().pageRequest().hasFilters()).isFalse();
    }

    @Test
    @DisplayName("Should build PageRequest with filter when filter and filterValue are provided")
    void shouldBuildPageRequestWithFilterWhenFilterAndFilterValueAreProvided() {
        var page = Page.<UserViewModel>of(List.of(), 0, 10, 0L);

        when(userController.findAll(any(FindAllUsersUseCase.Input.class))).thenReturn(page);

        restController.findAll(0, 10, null, null, "email", "john@example.com");

        var captor = ArgumentCaptor.forClass(FindAllUsersUseCase.Input.class);
        verify(userController, times(1)).findAll(captor.capture());
        var filters = captor.getValue().pageRequest().filters();
        assertThat(filters).hasSize(1);
        assertThat(filters.getFirst().field()).isEqualTo("email");
        assertThat(filters.getFirst().value()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("Should return empty page when no Users exist")
    void shouldReturnEmptyPageWhenNoUsersExist() {
        var page = Page.<UserViewModel>of(List.of(), 0, 10, 0L);

        when(userController.findAll(any(FindAllUsersUseCase.Input.class))).thenReturn(page);

        var response = restController.findAll(0, 10, null, null, null, null);

        assertThat(response.content()).isEmpty();
        assertThat(response.totalElements()).isZero();
    }
}

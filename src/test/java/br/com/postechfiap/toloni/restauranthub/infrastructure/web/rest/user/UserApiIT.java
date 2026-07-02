package br.com.postechfiap.toloni.restauranthub.infrastructure.web.rest.user;

import br.com.postechfiap.toloni.restauranthub.domain.usertype.UserRole;
import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.repositories.UserJpaRepository;
import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.repositories.UserTypeJpaRepository;
import br.com.postechfiap.toloni.restauranthub.infrastructure.web.rest.usertype.UserTypeRequest;
import br.com.postechfiap.toloni.restauranthub.infrastructure.web.rest.usertype.UserTypeResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserApiIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private UserTypeJpaRepository userTypeJpaRepository;

    private UUID restaurantOwnerTypeId;
    private UUID customerTypeId;

    @BeforeEach
    void setUp() throws Exception {
        userJpaRepository.deleteAll();
        userTypeJpaRepository.deleteAll();

        restaurantOwnerTypeId = createUserType("Restaurant Owner", "Owns and manages a restaurant", UserRole.RESTAURANT_OWNER);
        customerTypeId = createUserType("Customer", "Browses and orders food", UserRole.CUSTOMER);
    }

    // -------------------------------------------------------------------------
    // POST /api/v1/users
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should create User successfully")
    void shouldCreateUserSuccessfully() throws Exception {
        var request = new UserRequest("John Doe", "john@example.com", "password123", restaurantOwnerTypeId.toString());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.userTypeId").value(restaurantOwnerTypeId.toString()));
    }

    @Test
    @DisplayName("Should normalize email to lowercase on create")
    void shouldNormalizeEmailToLowercaseOnCreate() throws Exception {
        var request = new UserRequest("John Doe", "JOHN@EXAMPLE.COM", "password123", restaurantOwnerTypeId.toString());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @DisplayName("Should return 409 when email already exists")
    void shouldReturn409WhenEmailAlreadyExists() throws Exception {
        var request = new UserRequest("John Doe", "john@example.com", "password123", restaurantOwnerTypeId.toString());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        var duplicateRequest = new UserRequest("Jane Doe", "john@example.com", "password123", customerTypeId.toString());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.detail").value(containsString("john@example.com")));
    }

    @Test
    @DisplayName("Should return 404 when userTypeId does not exist")
    void shouldReturn404WhenUserTypeIdDoesNotExist() throws Exception {
        var request = new UserRequest("John Doe", "john@example.com", "password123", UUID.randomUUID().toString());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Should return 422 when email is invalid")
    void shouldReturn422WhenEmailIsInvalid() throws Exception {
        var request = new UserRequest("John Doe", "invalid-email", "password123", restaurantOwnerTypeId.toString());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.status").value(422));
    }

    @Test
    @DisplayName("Should return 422 when password is too short")
    void shouldReturn422WhenPasswordIsTooShort() throws Exception {
        var request = new UserRequest("John Doe", "john@example.com", "123", restaurantOwnerTypeId.toString());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.status").value(422));
    }

    @Test
    @DisplayName("Should return 422 when name is blank")
    void shouldReturn422WhenNameIsBlank() throws Exception {
        var request = new UserRequest("", "john@example.com", "password123", restaurantOwnerTypeId.toString());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.status").value(422));
    }

    // -------------------------------------------------------------------------
    // PATCH /api/v1/users/{id}
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should update User successfully")
    void shouldUpdateUserSuccessfully() throws Exception {
        var created = createUser("John Doe", "john@example.com", "password123", restaurantOwnerTypeId);
        var request = new UserRequest("Jane Doe", null, null, null);

        mockMvc.perform(patch("/api/v1/users/{id}", created.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @DisplayName("Should update User email successfully")
    void shouldUpdateUserEmailSuccessfully() throws Exception {
        var created = createUser("John Doe", "john@example.com", "password123", restaurantOwnerTypeId);
        var request = new UserRequest(null, "newjohn@example.com", null, null);

        mockMvc.perform(patch("/api/v1/users/{id}", created.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("newjohn@example.com"));
    }

    @Test
    @DisplayName("Should update User userTypeId successfully")
    void shouldUpdateUserUserTypeIdSuccessfully() throws Exception {
        var created = createUser("John Doe", "john@example.com", "password123", restaurantOwnerTypeId);
        var request = new UserRequest(null, null, null, customerTypeId.toString());

        mockMvc.perform(patch("/api/v1/users/{id}", created.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userTypeId").value(customerTypeId.toString()));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existing User")
    void shouldReturn404WhenUpdatingNonExistingUser() throws Exception {
        var request = new UserRequest("Jane Doe", null, null, null);

        mockMvc.perform(patch("/api/v1/users/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Should return 409 when updating to an already existing email")
    void shouldReturn409WhenUpdatingToAlreadyExistingEmail() throws Exception {
        createUser("John Doe", "john@example.com", "password123", restaurantOwnerTypeId);
        var jane = createUser("Jane Doe", "jane@example.com", "password123", customerTypeId);
        var request = new UserRequest(null, "john@example.com", null, null);

        mockMvc.perform(patch("/api/v1/users/{id}", jane.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("Should return 404 when updating with non-existing userTypeId")
    void shouldReturn404WhenUpdatingWithNonExistingUserTypeId() throws Exception {
        var created = createUser("John Doe", "john@example.com", "password123", restaurantOwnerTypeId);
        var request = new UserRequest(null, null, null, UUID.randomUUID().toString());

        mockMvc.perform(patch("/api/v1/users/{id}", created.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // -------------------------------------------------------------------------
    // DELETE /api/v1/users/{id}
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should delete User successfully")
    void shouldDeleteUserSuccessfully() throws Exception {
        var created = createUser("John Doe", "john@example.com", "password123", restaurantOwnerTypeId);

        mockMvc.perform(delete("/api/v1/users/{id}", created.id()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 204 when deleting non-existing User")
    void shouldReturn204WhenDeletingNonExistingUser() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    // -------------------------------------------------------------------------
    // GET /api/v1/users/{id}
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should find User by id successfully with userTypeName")
    void shouldFindUserByIdSuccessfullyWithUserTypeName() throws Exception {
        var created = createUser("John Doe", "john@example.com", "password123", restaurantOwnerTypeId);

        mockMvc.perform(get("/api/v1/users/{id}", created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.id().toString()))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.userTypeName").value("Restaurant Owner"));
    }

    @Test
    @DisplayName("Should return 404 when User is not found")
    void shouldReturn404WhenUserIsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/users/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // -------------------------------------------------------------------------
    // GET /api/v1/users
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should return paginated list of Users")
    void shouldReturnPaginatedListOfUsers() throws Exception {
        createUser("John Doe", "john@example.com", "password123", restaurantOwnerTypeId);
        createUser("Jane Doe", "jane@example.com", "password123", customerTypeId);

        mockMvc.perform(get("/api/v1/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    @DisplayName("Should return empty page when no Users exist")
    void shouldReturnEmptyPageWhenNoUsersExist() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    @DisplayName("Should return filtered Users by name")
    void shouldReturnFilteredUsersByName() throws Exception {
        createUser("John Doe", "john@example.com", "password123", restaurantOwnerTypeId);
        createUser("Jane Doe", "jane@example.com", "password123", customerTypeId);

        mockMvc.perform(get("/api/v1/users")
                        .param("filter", "name")
                        .param("filterValue", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("John Doe"));
    }

    @Test
    @DisplayName("Should return sorted Users by name ASC")
    void shouldReturnSortedUsersByNameAsc() throws Exception {
        createUser("John Doe", "john@example.com", "password123", restaurantOwnerTypeId);
        createUser("Jane Doe", "jane@example.com", "password123", customerTypeId);

        mockMvc.perform(get("/api/v1/users")
                        .param("sort", "name")
                        .param("direction", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Jane Doe"))
                .andExpect(jsonPath("$.content[1].name").value("John Doe"));
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private UUID createUserType(String name, String description, UserRole role) throws Exception {
        var request = new UserTypeRequest(name, description, role);

        var result = mockMvc.perform(post("/api/v1/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        var response = objectMapper.readValue(result.getResponse().getContentAsString(), UserTypeResponse.class);
        return response.id();
    }

    private UserResponse createUser(String name, String email, String password, UUID userTypeId) throws Exception {
        var request = new UserRequest(name, email, password, userTypeId.toString());

        var result = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
    }
}

package br.com.postechfiap.toloni.restauranthub.infrastructure.web.rest.restaurant;

import br.com.postechfiap.toloni.restauranthub.domain.usertype.UserRole;
import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.repositories.RestaurantJpaRepository;
import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.repositories.UserJpaRepository;
import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.repositories.UserTypeJpaRepository;
import br.com.postechfiap.toloni.restauranthub.infrastructure.web.rest.user.UserRequest;
import br.com.postechfiap.toloni.restauranthub.infrastructure.web.rest.user.UserResponse;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class RestaurantApiIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private UserTypeJpaRepository userTypeJpaRepository;

    private UUID ownerUserId;
    private UUID adminUserId;
    private UUID customerUserId;
    private UUID ownerTypeId;

    @BeforeEach
    void setUp() throws Exception {
        restaurantJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
        userTypeJpaRepository.deleteAll();

        ownerTypeId = createUserType("Restaurant Owner", "Owns and manages a restaurant", UserRole.RESTAURANT_OWNER);
        var adminTypeId = createUserType("Admin", "System administrator", UserRole.ADMIN);
        var customerTypeId = createUserType("Customer", "Browses and orders food", UserRole.CUSTOMER);

        ownerUserId = createUser("Owner User", "owner@example.com", "password123", ownerTypeId);
        adminUserId = createUser("Admin User", "admin@example.com", "password123", adminTypeId);
        customerUserId = createUser("Customer User", "customer@example.com", "password123", customerTypeId);
    }

    // -------------------------------------------------------------------------
    // POST /api/v1/restaurants
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should create Restaurant successfully")
    void shouldCreateRestaurantSuccessfully() throws Exception {
        var request = new RestaurantRequest("Burger House", "123 Main St", "American", "Mon-Fri 9am-10pm");

        mockMvc.perform(post("/api/v1/restaurants")
                        .header("X-User-Id", ownerUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Burger House"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.cuisineType").value("American"))
                .andExpect(jsonPath("$.openingHours").value("Mon-Fri 9am-10pm"))
                .andExpect(jsonPath("$.ownerId").value(ownerUserId.toString()));
    }

    @Test
    @DisplayName("Should return 409 when restaurant name already exists")
    void shouldReturn409WhenRestaurantNameAlreadyExists() throws Exception {
        var request = new RestaurantRequest("Burger House", "123 Main St", "American", "Mon-Fri 9am-10pm");

        mockMvc.perform(post("/api/v1/restaurants")
                        .header("X-User-Id", ownerUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/restaurants")
                        .header("X-User-Id", ownerUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("Should return 404 when owner user does not exist")
    void shouldReturn404WhenOwnerUserDoesNotExist() throws Exception {
        var request = new RestaurantRequest("Burger House", "123 Main St", "American", "Mon-Fri 9am-10pm");

        mockMvc.perform(post("/api/v1/restaurants")
                        .header("X-User-Id", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Should return 403 when user is not a restaurant owner")
    void shouldReturn403WhenUserIsNotARestaurantOwner() throws Exception {
        var request = new RestaurantRequest("Burger House", "123 Main St", "American", "Mon-Fri 9am-10pm");

        mockMvc.perform(post("/api/v1/restaurants")
                        .header("X-User-Id", customerUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    // -------------------------------------------------------------------------
    // PATCH /api/v1/restaurants/{id}
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should update Restaurant successfully")
    void shouldUpdateRestaurantSuccessfully() throws Exception {
        var created = createRestaurant("Burger House", "123 Main St", "American", "Mon-Fri 9am-10pm", ownerUserId);
        var request = new RestaurantRequest("Burger Palace", null, null, null);

        mockMvc.perform(patch("/api/v1/restaurants/{id}", created.id())
                        .header("X-User-Id", ownerUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Burger Palace"))
                .andExpect(jsonPath("$.address").value("123 Main St"));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existing Restaurant")
    void shouldReturn404WhenUpdatingNonExistingRestaurant() throws Exception {
        var request = new RestaurantRequest("Burger Palace", null, null, null);

        mockMvc.perform(patch("/api/v1/restaurants/{id}", UUID.randomUUID())
                        .header("X-User-Id", ownerUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Should return 409 when updating to an already existing restaurant name")
    void shouldReturn409WhenUpdatingToAlreadyExistingRestaurantName() throws Exception {
        createRestaurant("Burger House", "123 Main St", "American", "Mon-Fri 9am-10pm", ownerUserId);
        var second = createRestaurant("Pizza Place", "456 Oak Ave", "Italian", "Tue-Sun 11am-11pm", ownerUserId);
        var request = new RestaurantRequest("Burger House", null, null, null);

        mockMvc.perform(patch("/api/v1/restaurants/{id}", second.id())
                        .header("X-User-Id", ownerUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("Should return 403 when non-owner user tries to update restaurant")
    void shouldReturn403WhenNonOwnerUserTriesToUpdateRestaurant() throws Exception {
        var otherOwnerId = createUser("Other Owner", "other@example.com", "password123", ownerTypeId);
        var created = createRestaurant("Burger House", "123 Main St", "American", "Mon-Fri 9am-10pm", ownerUserId);
        var request = new RestaurantRequest("Burger Palace", null, null, null);

        mockMvc.perform(patch("/api/v1/restaurants/{id}", created.id())
                        .header("X-User-Id", otherOwnerId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    @DisplayName("Admin should update any restaurant")
    void adminShouldUpdateAnyRestaurant() throws Exception {
        var created = createRestaurant("Burger House", "123 Main St", "American", "Mon-Fri 9am-10pm", ownerUserId);
        var request = new RestaurantRequest("Burger Palace", null, null, null);

        mockMvc.perform(patch("/api/v1/restaurants/{id}", created.id())
                        .header("X-User-Id", adminUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Burger Palace"));
    }

    // -------------------------------------------------------------------------
    // PATCH /api/v1/restaurants/{id}/transfer-ownership
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Admin should transfer restaurant ownership successfully")
    void adminShouldTransferRestaurantOwnershipSuccessfully() throws Exception {
        var newOwnerId = createUser("New Owner", "newowner@example.com", "password123", ownerTypeId);
        var created = createRestaurant("Burger House", "123 Main St", "American", "Mon-Fri 9am-10pm", ownerUserId);
        var request = new RestaurantTransferOwnershipRequest(newOwnerId.toString());

        mockMvc.perform(patch("/api/v1/restaurants/{id}/transfer-ownership", created.id())
                        .header("X-User-Id", adminUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newOwnerId").value(newOwnerId.toString()));
    }

    @Test
    @DisplayName("Should return 403 when non-admin tries to transfer ownership")
    void shouldReturn403WhenNonAdminTriesToTransferOwnership() throws Exception {
        var newOwnerId = createUser("New Owner", "newowner@example.com", "password123", ownerTypeId);
        var created = createRestaurant("Burger House", "123 Main St", "American", "Mon-Fri 9am-10pm", ownerUserId);
        var request = new RestaurantTransferOwnershipRequest(newOwnerId.toString());

        mockMvc.perform(patch("/api/v1/restaurants/{id}/transfer-ownership", created.id())
                        .header("X-User-Id", ownerUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    @DisplayName("Should return 403 when new owner is not a restaurant owner role")
    void shouldReturn403WhenNewOwnerIsNotRestaurantOwnerRole() throws Exception {
        var created = createRestaurant("Burger House", "123 Main St", "American", "Mon-Fri 9am-10pm", ownerUserId);
        var request = new RestaurantTransferOwnershipRequest(customerUserId.toString());

        mockMvc.perform(patch("/api/v1/restaurants/{id}/transfer-ownership", created.id())
                        .header("X-User-Id", adminUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    // -------------------------------------------------------------------------
    // DELETE /api/v1/restaurants/{id}
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should delete Restaurant successfully")
    void shouldDeleteRestaurantSuccessfully() throws Exception {
        var created = createRestaurant("Burger House", "123 Main St", "American", "Mon-Fri 9am-10pm", ownerUserId);

        mockMvc.perform(delete("/api/v1/restaurants/{id}", created.id())
                        .header("X-User-Id", ownerUserId.toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existing Restaurant")
    void shouldReturn204WhenDeletingNonExistingRestaurant() throws Exception {
        mockMvc.perform(delete("/api/v1/restaurants/{id}", UUID.randomUUID())
                        .header("X-User-Id", ownerUserId.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 403 when non-owner user tries to delete restaurant")
    void shouldReturn403WhenNonOwnerUserTriesToDeleteRestaurant() throws Exception {
        var otherOwnerId = createUser("Other Owner", "other@example.com", "password123", ownerTypeId);
        var created = createRestaurant("Burger House", "123 Main St", "American", "Mon-Fri 9am-10pm", ownerUserId);

        mockMvc.perform(delete("/api/v1/restaurants/{id}", created.id())
                        .header("X-User-Id", otherOwnerId.toString()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    // -------------------------------------------------------------------------
    // GET /api/v1/restaurants/{id}
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should find Restaurant by id successfully with ownerName")
    void shouldFindRestaurantByIdSuccessfullyWithOwnerName() throws Exception {
        var created = createRestaurant("Burger House", "123 Main St", "American", "Mon-Fri 9am-10pm", ownerUserId);

        mockMvc.perform(get("/api/v1/restaurants/{id}", created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.id().toString()))
                .andExpect(jsonPath("$.name").value("Burger House"))
                .andExpect(jsonPath("$.ownerName").value("Owner User"));
    }

    @Test
    @DisplayName("Should return 404 when Restaurant is not found")
    void shouldReturn404WhenRestaurantIsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // -------------------------------------------------------------------------
    // GET /api/v1/restaurants
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should return paginated list of Restaurants")
    void shouldReturnPaginatedListOfRestaurants() throws Exception {
        createRestaurant("Burger House", "123 Main St", "American", "Mon-Fri 9am-10pm", ownerUserId);
        createRestaurant("Pizza Place", "456 Oak Ave", "Italian", "Tue-Sun 11am-11pm", ownerUserId);

        mockMvc.perform(get("/api/v1/restaurants")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    @DisplayName("Should return empty page when no Restaurants exist")
    void shouldReturnEmptyPageWhenNoRestaurantsExist() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    @DisplayName("Should return filtered Restaurants by name")
    void shouldReturnFilteredRestaurantsByName() throws Exception {
        createRestaurant("Burger House", "123 Main St", "American", "Mon-Fri 9am-10pm", ownerUserId);
        createRestaurant("Pizza Place", "456 Oak Ave", "Italian", "Tue-Sun 11am-11pm", ownerUserId);

        mockMvc.perform(get("/api/v1/restaurants")
                        .param("filter", "name")
                        .param("filterValue", "Burger"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("Burger House"));
    }

    @Test
    @DisplayName("Should return sorted Restaurants by name ASC")
    void shouldReturnSortedRestaurantsByNameAsc() throws Exception {
        createRestaurant("Pizza Place", "456 Oak Ave", "Italian", "Tue-Sun 11am-11pm", ownerUserId);
        createRestaurant("Burger House", "123 Main St", "American", "Mon-Fri 9am-10pm", ownerUserId);

        mockMvc.perform(get("/api/v1/restaurants")
                        .param("sort", "name")
                        .param("direction", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Burger House"))
                .andExpect(jsonPath("$.content[1].name").value("Pizza Place"));
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

    private UUID createUser(String name, String email, String password, UUID userTypeId) throws Exception {
        var request = new UserRequest(name, email, password, userTypeId.toString());

        var result = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        var response = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
        return response.id();
    }

    private RestaurantResponse createRestaurant(String name, String address, String cuisineType,
                                                String openingHours, UUID ownerId) throws Exception {
        var request = new RestaurantRequest(name, address, cuisineType, openingHours);

        var result = mockMvc.perform(post("/api/v1/restaurants")
                        .header("X-User-Id", ownerId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(), RestaurantResponse.class);
    }
}

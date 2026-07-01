package br.com.postechfiap.toloni.restauranthub.infrastructure.web.rest.menuitem;

import br.com.postechfiap.toloni.restauranthub.domain.usertype.UserRole;
import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.repositories.MenuItemJpaRepository;
import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.repositories.RestaurantJpaRepository;
import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.repositories.UserJpaRepository;
import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.repositories.UserTypeJpaRepository;
import br.com.postechfiap.toloni.restauranthub.infrastructure.web.rest.restaurant.RestaurantRequest;
import br.com.postechfiap.toloni.restauranthub.infrastructure.web.rest.restaurant.RestaurantResponse;
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

import java.math.BigDecimal;
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
class MenuItemApiIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MenuItemJpaRepository menuItemJpaRepository;

    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private UserTypeJpaRepository userTypeJpaRepository;

    private UUID ownerUserId;
    private UUID otherOwnerUserId;
    private UUID adminUserId;
    private UUID customerUserId;
    private UUID ownerTypeId;
    private UUID restaurantId;

    @BeforeEach
    void setUp() throws Exception {
        menuItemJpaRepository.deleteAll();
        restaurantJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
        userTypeJpaRepository.deleteAll();

        ownerTypeId = createUserType("Restaurant Owner", "Owns and manages a restaurant", UserRole.RESTAURANT_OWNER);
        var adminTypeId = createUserType("Admin", "System administrator", UserRole.ADMIN);
        var customerTypeId = createUserType("Customer", "Browses and orders food", UserRole.CUSTOMER);

        ownerUserId = createUser("Owner User", "owner@example.com", "password123", ownerTypeId);
        otherOwnerUserId = createUser("Other Owner", "other@example.com", "password123", ownerTypeId);
        adminUserId = createUser("Admin User", "admin@example.com", "password123", adminTypeId);
        customerUserId = createUser("Customer User", "customer@example.com", "password123", customerTypeId);

        restaurantId = createRestaurant("Burger House", "123 Main St", "American", "Mon-Fri 9am-10pm", ownerUserId);
    }

    // -------------------------------------------------------------------------
    // POST /api/v1/menu-items
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should create MenuItem successfully")
    void shouldCreateMenuItemSuccessfully() throws Exception {
        var request = new MenuItemRequest("Classic Burger", "Juicy beef burger", new BigDecimal("19.90"),
                "BRL", false, null, restaurantId.toString());

        mockMvc.perform(post("/api/v1/menu-items")
                        .header("X-User-Id", ownerUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Classic Burger"))
                .andExpect(jsonPath("$.description").value("Juicy beef burger"))
                .andExpect(jsonPath("$.price").value(19.90))
                .andExpect(jsonPath("$.currency").value("BRL"))
                .andExpect(jsonPath("$.dineInOnly").value(false))
                .andExpect(jsonPath("$.restaurantId").value(restaurantId.toString()));
    }

    @Test
    @DisplayName("Should return 409 when menu item name already exists in restaurant")
    void shouldReturn409WhenMenuItemNameAlreadyExistsInRestaurant() throws Exception {
        var request = new MenuItemRequest("Classic Burger", "Juicy beef burger", new BigDecimal("19.90"),
                "BRL", false, null, restaurantId.toString());

        mockMvc.perform(post("/api/v1/menu-items")
                        .header("X-User-Id", ownerUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/menu-items")
                        .header("X-User-Id", ownerUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("Should return 404 when restaurant does not exist")
    void shouldReturn404WhenRestaurantDoesNotExist() throws Exception {
        var request = new MenuItemRequest("Classic Burger", "Juicy beef burger", new BigDecimal("19.90"),
                "BRL", false, null, UUID.randomUUID().toString());

        mockMvc.perform(post("/api/v1/menu-items")
                        .header("X-User-Id", ownerUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Should return 403 when user is not the owner of the restaurant")
    void shouldReturn403WhenUserIsNotOwnerOfRestaurant() throws Exception {
        var request = new MenuItemRequest("Classic Burger", "Juicy beef burger", new BigDecimal("19.90"),
                "BRL", false, null, restaurantId.toString());

        mockMvc.perform(post("/api/v1/menu-items")
                        .header("X-User-Id", otherOwnerUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    @DisplayName("Admin should create menu item for any restaurant")
    void adminShouldCreateMenuItemForAnyRestaurant() throws Exception {
        var request = new MenuItemRequest("Classic Burger", "Juicy beef burger", new BigDecimal("19.90"),
                "BRL", false, null, restaurantId.toString());

        mockMvc.perform(post("/api/v1/menu-items")
                        .header("X-User-Id", adminUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Classic Burger"));
    }

    // -------------------------------------------------------------------------
    // PATCH /api/v1/menu-items/{id}
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should update MenuItem successfully")
    void shouldUpdateMenuItemSuccessfully() throws Exception {
        var created = createMenuItem("Classic Burger", "Juicy beef burger", new BigDecimal("19.90"),
                "BRL", false, restaurantId, ownerUserId);
        var request = new MenuItemRequest("Double Burger", null, new BigDecimal("29.90"),
                "BRL", null, null, null);

        mockMvc.perform(patch("/api/v1/menu-items/{id}", created.id())
                        .header("X-User-Id", ownerUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Double Burger"))
                .andExpect(jsonPath("$.price").value(29.90))
                .andExpect(jsonPath("$.description").value("Juicy beef burger"));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existing MenuItem")
    void shouldReturn404WhenUpdatingNonExistingMenuItem() throws Exception {
        var request = new MenuItemRequest("Double Burger", null, null, null, null, null, null);

        mockMvc.perform(patch("/api/v1/menu-items/{id}", UUID.randomUUID())
                        .header("X-User-Id", ownerUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Should return 409 when updating to an already existing menu item name")
    void shouldReturn409WhenUpdatingToAlreadyExistingMenuItemName() throws Exception {
        createMenuItem("Classic Burger", "Juicy beef burger", new BigDecimal("19.90"),
                "BRL", false, restaurantId, ownerUserId);
        var veggie = createMenuItem("Veggie Burger", "Plant-based burger", new BigDecimal("22.90"),
                "BRL", false, restaurantId, ownerUserId);
        var request = new MenuItemRequest("Classic Burger", null, null, null, null, null, null);

        mockMvc.perform(patch("/api/v1/menu-items/{id}", veggie.id())
                        .header("X-User-Id", ownerUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("Should return 403 when non-owner tries to update menu item")
    void shouldReturn403WhenNonOwnerTriesToUpdateMenuItem() throws Exception {
        var created = createMenuItem("Classic Burger", "Juicy beef burger", new BigDecimal("19.90"),
                "BRL", false, restaurantId, ownerUserId);
        var request = new MenuItemRequest("Double Burger", null, null, null, null, null, null);

        mockMvc.perform(patch("/api/v1/menu-items/{id}", created.id())
                        .header("X-User-Id", otherOwnerUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    @DisplayName("Admin should update any menu item")
    void adminShouldUpdateAnyMenuItem() throws Exception {
        var created = createMenuItem("Classic Burger", "Juicy beef burger", new BigDecimal("19.90"),
                "BRL", false, restaurantId, ownerUserId);
        var request = new MenuItemRequest("Double Burger", null, null, null, null, null, null);

        mockMvc.perform(patch("/api/v1/menu-items/{id}", created.id())
                        .header("X-User-Id", adminUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Double Burger"));
    }

    // -------------------------------------------------------------------------
    // DELETE /api/v1/menu-items/{id}
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should delete MenuItem successfully")
    void shouldDeleteMenuItemSuccessfully() throws Exception {
        var created = createMenuItem("Classic Burger", "Juicy beef burger", new BigDecimal("19.90"),
                "BRL", false, restaurantId, ownerUserId);

        mockMvc.perform(delete("/api/v1/menu-items/{id}", created.id())
                        .header("X-User-Id", ownerUserId.toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existing MenuItem")
    void shouldReturn404WhenDeletingNonExistingMenuItem() throws Exception {
        mockMvc.perform(delete("/api/v1/menu-items/{id}", UUID.randomUUID())
                        .header("X-User-Id", ownerUserId.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Should return 403 when non-owner tries to delete menu item")
    void shouldReturn403WhenNonOwnerTriesToDeleteMenuItem() throws Exception {
        var created = createMenuItem("Classic Burger", "Juicy beef burger", new BigDecimal("19.90"),
                "BRL", false, restaurantId, ownerUserId);

        mockMvc.perform(delete("/api/v1/menu-items/{id}", created.id())
                        .header("X-User-Id", otherOwnerUserId.toString()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    // -------------------------------------------------------------------------
    // GET /api/v1/menu-items/{id}
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should find MenuItem by id successfully with restaurantName")
    void shouldFindMenuItemByIdSuccessfullyWithRestaurantName() throws Exception {
        var created = createMenuItem("Classic Burger", "Juicy beef burger", new BigDecimal("19.90"),
                "BRL", false, restaurantId, ownerUserId);

        mockMvc.perform(get("/api/v1/menu-items/{id}", created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.id().toString()))
                .andExpect(jsonPath("$.name").value("Classic Burger"))
                .andExpect(jsonPath("$.restaurantId").value(restaurantId.toString()))
                .andExpect(jsonPath("$.restaurantName").value("Burger House"));
    }

    @Test
    @DisplayName("Should return 404 when MenuItem is not found")
    void shouldReturn404WhenMenuItemIsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/menu-items/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // -------------------------------------------------------------------------
    // GET /api/v1/menu-items
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should return paginated list of MenuItems")
    void shouldReturnPaginatedListOfMenuItems() throws Exception {
        createMenuItem("Classic Burger", "Juicy beef burger", new BigDecimal("19.90"),
                "BRL", false, restaurantId, ownerUserId);
        createMenuItem("Veggie Burger", "Plant-based burger", new BigDecimal("22.90"),
                "BRL", false, restaurantId, ownerUserId);

        mockMvc.perform(get("/api/v1/menu-items")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    @DisplayName("Should return empty page when no MenuItems exist")
    void shouldReturnEmptyPageWhenNoMenuItemsExist() throws Exception {
        mockMvc.perform(get("/api/v1/menu-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    @DisplayName("Should return MenuItems filtered by restaurantId")
    void shouldReturnMenuItemsFilteredByRestaurantId() throws Exception {
        var otherRestaurantId = createRestaurant("Pizza Place", "456 Oak Ave", "Italian",
                "Tue-Sun 11am-11pm", otherOwnerUserId);
        createMenuItem("Classic Burger", "Juicy beef burger", new BigDecimal("19.90"),
                "BRL", false, restaurantId, ownerUserId);
        createMenuItem("Margherita Pizza", "Classic tomato pizza", new BigDecimal("35.00"),
                "BRL", false, otherRestaurantId, otherOwnerUserId);

        mockMvc.perform(get("/api/v1/menu-items")
                        .param("restaurantId", restaurantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("Classic Burger"));
    }

    @Test
    @DisplayName("Should return filtered MenuItems by name")
    void shouldReturnFilteredMenuItemsByName() throws Exception {
        createMenuItem("Classic Burger", "Juicy beef burger", new BigDecimal("19.90"),
                "BRL", false, restaurantId, ownerUserId);
        createMenuItem("Veggie Burger", "Plant-based burger", new BigDecimal("22.90"),
                "BRL", false, restaurantId, ownerUserId);

        mockMvc.perform(get("/api/v1/menu-items")
                        .param("filter", "name")
                        .param("filterValue", "Classic"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("Classic Burger"));
    }

    @Test
    @DisplayName("Should return sorted MenuItems by name ASC")
    void shouldReturnSortedMenuItemsByNameAsc() throws Exception {
        createMenuItem("Veggie Burger", "Plant-based burger", new BigDecimal("22.90"),
                "BRL", false, restaurantId, ownerUserId);
        createMenuItem("Classic Burger", "Juicy beef burger", new BigDecimal("19.90"),
                "BRL", false, restaurantId, ownerUserId);

        mockMvc.perform(get("/api/v1/menu-items")
                        .param("sort", "name")
                        .param("direction", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Classic Burger"))
                .andExpect(jsonPath("$.content[1].name").value("Veggie Burger"));
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

    private UUID createRestaurant(String name, String address, String cuisineType,
                                  String openingHours, UUID ownerId) throws Exception {
        var request = new RestaurantRequest(name, address, cuisineType, openingHours);

        var result = mockMvc.perform(post("/api/v1/restaurants")
                        .header("X-User-Id", ownerId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        var response = objectMapper.readValue(result.getResponse().getContentAsString(), RestaurantResponse.class);
        return response.id();
    }

    private MenuItemResponse createMenuItem(String name, String description, BigDecimal price,
                                            String currency, boolean dineInOnly,
                                            UUID restaurantId, UUID ownerId) throws Exception {
        var request = new MenuItemRequest(name, description, price, currency, dineInOnly, null, restaurantId.toString());

        var result = mockMvc.perform(post("/api/v1/menu-items")
                        .header("X-User-Id", ownerId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(), MenuItemResponse.class);
    }
}

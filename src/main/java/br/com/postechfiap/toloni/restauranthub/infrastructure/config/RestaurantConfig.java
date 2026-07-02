package br.com.postechfiap.toloni.restauranthub.infrastructure.config;

import br.com.postechfiap.toloni.restauranthub.adapters.controllers.RestaurantController;
import br.com.postechfiap.toloni.restauranthub.adapters.presenters.restaurant.RestaurantPresenter;
import br.com.postechfiap.toloni.restauranthub.application.authorization.AuthorizationService;
import br.com.postechfiap.toloni.restauranthub.application.gateways.MenuItemGateway;
import br.com.postechfiap.toloni.restauranthub.application.gateways.RestaurantGateway;
import br.com.postechfiap.toloni.restauranthub.application.gateways.UserGateway;
import br.com.postechfiap.toloni.restauranthub.application.usecases.restaurant.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/// Spring configuration for [Restaurant] beans.
@Configuration
public class RestaurantConfig {

    @Bean
    public CreateRestaurantUseCase createRestaurantUseCase(RestaurantGateway restaurantGateway,
                                                           AuthorizationService authorizationService) {
        return new CreateRestaurantUseCase(restaurantGateway, authorizationService);
    }

    @Bean
    public UpdateRestaurantUseCase updateRestaurantUseCase(RestaurantGateway restaurantGateway,
                                                           UserGateway userGateway,
                                                           AuthorizationService authorizationService) {
        return new UpdateRestaurantUseCase(restaurantGateway, userGateway, authorizationService);
    }

    @Bean
    public DeleteRestaurantUseCase deleteRestaurantUseCase(RestaurantGateway restaurantGateway,
                                                           MenuItemGateway menuItemGateway,
                                                           AuthorizationService authorizationService) {
        return new DeleteRestaurantUseCase(restaurantGateway, menuItemGateway, authorizationService);
    }

    @Bean
    public FindRestaurantByIdUseCase findRestaurantByIdUseCase(RestaurantGateway restaurantGateway) {
        return new FindRestaurantByIdUseCase(restaurantGateway);
    }

    @Bean
    public FindAllRestaurantsUseCase findAllRestaurantsUseCase(RestaurantGateway restaurantGateway) {
        return new FindAllRestaurantsUseCase(restaurantGateway);
    }

    @Bean
    public TransferRestaurantOwnershipUseCase transferRestaurantOwnershipUseCase(
            RestaurantGateway restaurantGateway,
            AuthorizationService authorizationService) {
        return new TransferRestaurantOwnershipUseCase(restaurantGateway, authorizationService);
    }

    @Bean
    public RestaurantPresenter restaurantPresenter() {
        return new RestaurantPresenter();
    }

    @Bean
    public RestaurantController restaurantController(
            CreateRestaurantUseCase createRestaurantUseCase,
            UpdateRestaurantUseCase updateRestaurantUseCase,
            DeleteRestaurantUseCase deleteRestaurantUseCase,
            FindRestaurantByIdUseCase findRestaurantByIdUseCase,
            FindAllRestaurantsUseCase findAllRestaurantsUseCase,
            TransferRestaurantOwnershipUseCase transferRestaurantOwnershipUseCase,
            RestaurantPresenter restaurantPresenter) {
        return new RestaurantController(
                createRestaurantUseCase,
                updateRestaurantUseCase,
                deleteRestaurantUseCase,
                findRestaurantByIdUseCase,
                findAllRestaurantsUseCase,
                transferRestaurantOwnershipUseCase,
                restaurantPresenter
        );
    }
}

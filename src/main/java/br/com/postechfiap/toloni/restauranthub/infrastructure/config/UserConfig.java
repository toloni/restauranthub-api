package br.com.postechfiap.toloni.restauranthub.infrastructure.config;

import br.com.postechfiap.toloni.restauranthub.adapters.controllers.UserController;
import br.com.postechfiap.toloni.restauranthub.adapters.presenters.user.UserPresenter;
import br.com.postechfiap.toloni.restauranthub.application.gateways.RestaurantGateway;
import br.com.postechfiap.toloni.restauranthub.application.gateways.UserGateway;
import br.com.postechfiap.toloni.restauranthub.application.gateways.UserTypeGateway;
import br.com.postechfiap.toloni.restauranthub.application.usecases.user.*;
import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.gateways.UserGatewayImpl;
import br.com.postechfiap.toloni.restauranthub.infrastructure.persistence.repositories.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/// Spring configuration for [User] beans.
@Configuration
public class UserConfig {

    @Bean
    public UserGateway userGateway(UserJpaRepository userJpaRepository) {
        return new UserGatewayImpl(userJpaRepository);
    }

    @Bean
    public CreateUserUseCase createUserUseCase(UserGateway userGateway, UserTypeGateway userTypeGateway) {
        return new CreateUserUseCase(userGateway, userTypeGateway);
    }

    @Bean
    public FindUserByIdUseCase getUserByIdUseCase(UserGateway userGateway) {
        return new FindUserByIdUseCase(userGateway);
    }

    @Bean
    public FindAllUsersUseCase getAllUsersUseCase(UserGateway userGateway) {
        return new FindAllUsersUseCase(userGateway);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase(UserGateway userGateway, UserTypeGateway userTypeGateway) {
        return new UpdateUserUseCase(userGateway, userTypeGateway);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserGateway userGateway, RestaurantGateway restaurantGateway) {
        return new DeleteUserUseCase(userGateway, restaurantGateway);
    }

    @Bean
    public UserPresenter userPresenter() {
        return new UserPresenter();
    }

    @Bean
    public UserController userController(
            CreateUserUseCase createUserUseCase,
            UpdateUserUseCase updateUserUseCase,
            DeleteUserUseCase deleteUserUseCase,
            FindUserByIdUseCase findUserByIdUseCase,
            FindAllUsersUseCase findAllUsersUseCase,
            UserPresenter userPresenter
    ) {
        return new UserController(
                createUserUseCase,
                updateUserUseCase,
                deleteUserUseCase,
                findUserByIdUseCase,
                findAllUsersUseCase,
                userPresenter);
    }

}

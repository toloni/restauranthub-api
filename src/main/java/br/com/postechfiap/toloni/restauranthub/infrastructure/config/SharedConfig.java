package br.com.postechfiap.toloni.restauranthub.infrastructure.config;

import br.com.postechfiap.toloni.restauranthub.application.authorization.AuthorizationService;
import br.com.postechfiap.toloni.restauranthub.application.gateways.UserGateway;
import br.com.postechfiap.toloni.restauranthub.application.gateways.UserTypeGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/// Spring configuration for shared cross-cutting beans.
@Configuration
public class SharedConfig {

    @Bean
    public AuthorizationService authorizationService(UserGateway userGateway,
                                                     UserTypeGateway userTypeGateway) {
        return new AuthorizationService(userGateway, userTypeGateway);
    }
}

package com.github.krystiankowalik.splitme.api.usersservice.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class KeycloakConfig {


    @Value("${keycloak.auth-server-url}")
    private String KEYCLOAK_SERVER_URL;

    @Value("${keycloak.realm}")
    private String KEYCLOAK_REALM;

    @Value("${kc.admin.username}")
    private String KEYCLOAK_ADMIN_USERNAME;

    @Value("${kc.admin.password}")
    private String KEYCLOAK_ADMIN_PASSWORD;

    @Value("${kc.admin.clientId}")
    private String KEYCLOAK_ADMIN_CLIENT_ID;

    @Autowired
    KeycloakClientRequestFactory keycloakClientRequestFactory;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public KeycloakRestTemplate keycloakRestTemplate() {
        return new KeycloakRestTemplate(keycloakClientRequestFactory);
    }

    @Bean
    public Keycloak keycloak() {

        return KeycloakBuilder.builder() //
                .serverUrl(KEYCLOAK_SERVER_URL) //
                .realm(KEYCLOAK_REALM)//
                .username(KEYCLOAK_ADMIN_USERNAME) //
                .password(KEYCLOAK_ADMIN_PASSWORD) //
                .clientId(KEYCLOAK_ADMIN_CLIENT_ID) //
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()) //
                .build();
    }

    @Bean
    @Autowired
    public RealmResource realm(Keycloak keycloak) {
        return keycloak.realm(KEYCLOAK_REALM);
    }

}

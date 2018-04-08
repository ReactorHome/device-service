package com.myreactorhome.deviceservice.feign_clients;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;

import java.util.ArrayList;

@Configuration
public class EventClientConfiguration {

    @Value("${reactor.accessTokenUri}")
    private String tokenUrl;

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String secret;

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(){
        OAuth2ClientContext oauth2ClientContext = new DefaultOAuth2ClientContext();
        return new OAuth2FeignRequestInterceptor(oauth2ClientContext, resource());
    }

    @Bean
    protected OAuth2ProtectedResourceDetails resource() {
        ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
        resource.setAccessTokenUri(tokenUrl);
        resource.setClientId(clientId);
        resource.setClientSecret(secret);
        resource.setClientAuthenticationScheme(AuthenticationScheme.form);
        resource.setAuthenticationScheme(AuthenticationScheme.form);
        return resource;
    }
}

package com.myreactorhome.deviceservice.feign_clients;

import feign.RequestTemplate;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

public class OAuth2Interceptor extends OAuth2FeignRequestInterceptor {
    public OAuth2Interceptor(OAuth2ClientContext oAuth2ClientContext, OAuth2ProtectedResourceDetails resource) {
        super(oAuth2ClientContext, resource, "Bearer", "Authorization");
    }

    @Override
    public void apply(RequestTemplate template) {
        if(!template.headers().containsKey("Authorization")){
            template.header("Authorization", this.extract("Bearer"));
        }
    }
}

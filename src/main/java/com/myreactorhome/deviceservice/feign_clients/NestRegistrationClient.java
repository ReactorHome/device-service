package com.myreactorhome.deviceservice.feign_clients;

import feign.Param;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(value = "nest-registration", url = "https://api.home.nest.com", configuration = NestRegistrationClientConfiguration.class)
public interface NestRegistrationClient {

    @RequestMapping(path = "/oauth2/access_token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, method = RequestMethod.POST)
    Map<String, Object> register(Map<String, ?> formParams);
    //@Param("client_id") String clientId, @Param("client_secret") String clientSecret, @Param("code") String code, @Param("grant_type") String grantType
}

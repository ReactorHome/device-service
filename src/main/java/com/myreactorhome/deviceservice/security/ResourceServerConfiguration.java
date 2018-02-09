package com.myreactorhome.deviceservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myreactorhome.deviceservice.models.Account;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

import java.io.IOException;
import java.util.Map;

@EnableResourceServer
@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "devices";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(false);
        resources.tokenServices(tokenService());
    }
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        //.antMatchers("/user").permitAll()
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated();

        http.csrf().disable();
    }


    @Bean
    public RemoteTokenServices tokenServices() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(
                "https://api.myreactorhome.com/user/oauth/check_token");
        tokenService.setClientId("api-user");
        tokenService.setClientSecret("123456");
        return tokenService;
    }

    @Primary
    @Bean
    public UserInfoTokenServices tokenService(){
        UserInfoTokenServices userInfoTokenServices = new UserInfoTokenServices( "https://api.myreactorhome.com/user/api/users/me", "api-user");
        userInfoTokenServices.setPrincipalExtractor(customPrincipalExtractor());
        return userInfoTokenServices;
    }

    @Bean
    public PrincipalExtractor customPrincipalExtractor() {
        return new CustomPrincipalExtractor();
    }


    public class CustomPrincipalExtractor implements PrincipalExtractor {

        private final String[] PRINCIPAL_KEYS = new String[] {
                "account"};

        @Override
        public Object extractPrincipal(Map<String, Object> map) {
            for (String key : PRINCIPAL_KEYS) {
                if (map.containsKey(key)) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        String json = objectMapper.writeValueAsString(map.get(key));
                        Account a = objectMapper.readValue(json, Account.class);
                        return a;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }
            return null;
        }

    }
}

package com.myreactorhome.deviceservice;

import com.myreactorhome.deviceservice.models.Account;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public Account test(@AuthenticationPrincipal Account account){
        return account;
        //return account
    }
}

package com.myreactorhome.deviceservice.controllers;

import com.myreactorhome.deviceservice.models.Account;
import com.myreactorhome.deviceservice.models.GenericDevice;
import com.myreactorhome.deviceservice.models.Hub;
import com.myreactorhome.deviceservice.repositories.HubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HubController {

    @Autowired
    private HubRepository hubRepository;

    @GetMapping("api/{id}/hub")
    @PreAuthorize("isGroupMember(#id)")
    public Hub index(@PathVariable("id") String id) {
        Hub hub = hubRepository.findOne(id);
        return hub;
    }

    @PostMapping("api/hub")
    public String createHub(@RequestBody Hub hub) {
        hub.setConnected(true);
        hubRepository.save(hub);
        return hub.getId();
    }
}

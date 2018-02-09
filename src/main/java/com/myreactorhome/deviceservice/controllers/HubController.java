package com.myreactorhome.deviceservice.controllers;

import com.myreactorhome.deviceservice.models.GenericDevice;
import com.myreactorhome.deviceservice.models.Hub;
import com.myreactorhome.deviceservice.repositories.HubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HubController {

    @Autowired
    private HubRepository hubRepository;

    @GetMapping("api/hub/{id}")
    public Hub index(@PathVariable("id") String id) {

        Hub hub = hubRepository.findOne(id);
        return hub;
    }

    @PostMapping("api/hub")
    public ResponseEntity<?> index(@RequestBody Hub hub) {
        hubRepository.save(hub);

        return new ResponseEntity(HttpStatus.CREATED);
    }
}

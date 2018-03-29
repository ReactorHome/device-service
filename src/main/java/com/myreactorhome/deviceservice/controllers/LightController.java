package com.myreactorhome.deviceservice.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myreactorhome.deviceservice.feign_clients.EventClient;
import com.myreactorhome.deviceservice.models.Hub;
import com.myreactorhome.deviceservice.models.Light;
import com.myreactorhome.deviceservice.models.Outlet;
import com.myreactorhome.deviceservice.repositories.HubRepository;
import com.myreactorhome.deviceservice.repositories.LightRepository;
import com.myreactorhome.deviceservice.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class LightController {

    @Autowired
    MessageService messageService;

    @Autowired
    HubRepository hubRepository;

    @Autowired
    LightRepository lightRepository;

    @Autowired
    private EventClient eventClient;

    @PatchMapping("/api/{id}/light/")
    public ResponseEntity<?> update(@PathVariable("id") String hubId, @RequestBody Light light){
        Hub hub = hubRepository.findOne(hubId);
        if (!hub.isConnected()){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        Optional<Light> repositoryLight = lightRepository.findByHardwareIdIs(light.getHardwareId());
        if (!repositoryLight.isPresent()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if (!repositoryLight.get().getConnected()){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            messageService.sendMessage(hub.getHardwareId(), objectMapper.writeValueAsString(light));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        eventClient.createEvent(hub.getGroupId(), light.getId());

        return new ResponseEntity(HttpStatus.OK);
    }
}

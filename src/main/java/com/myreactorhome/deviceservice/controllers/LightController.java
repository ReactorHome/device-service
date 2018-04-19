package com.myreactorhome.deviceservice.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myreactorhome.deviceservice.feign_clients.EventClient;
import com.myreactorhome.deviceservice.models.*;
import com.myreactorhome.deviceservice.repositories.HubRepository;
import com.myreactorhome.deviceservice.repositories.LightRepository;
import com.myreactorhome.deviceservice.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
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

        eventClient.createEvent(hub.getGroupId(), repositoryLight.get().getName());

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/api/{id}/bridge")
    public ResponseEntity<?> registerBridge(@PathVariable("id") String hubId, @RequestBody String serial){
        Hub hub = hubRepository.findOne(hubId);
        if (!hub.isConnected()){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        HueBridge bridge = new HueBridge();
        bridge.setHardwareId(serial);

        HubMessage message = new HubMessage(HubMessageType.REGISTER_BRIDGE, bridge);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try{
            messageService.sendMessage(hub.getHardwareId(), objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}

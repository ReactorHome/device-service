package com.myreactorhome.deviceservice.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myreactorhome.deviceservice.feign_clients.EventClient;
import com.myreactorhome.deviceservice.models.Hub;
import com.myreactorhome.deviceservice.models.HubMessage;
import com.myreactorhome.deviceservice.models.HubMessageType;
import com.myreactorhome.deviceservice.models.Outlet;
import com.myreactorhome.deviceservice.repositories.HubRepository;
import com.myreactorhome.deviceservice.repositories.OutletRepository;
import com.myreactorhome.deviceservice.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class OutletController {

    @Autowired
    OutletRepository outletRepository;

    @Autowired
    MessageService messageService;

    @Autowired
    HubRepository hubRepository;

    @Autowired
    private EventClient eventClient;

    @PatchMapping("/api/{id}/outlet/")
    public ResponseEntity<?> update(@PathVariable("id") String hubId, @RequestBody Outlet outlet){
        Hub hub = hubRepository.findOne(hubId);
        if (!hub.isConnected()){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        Optional<Outlet> repositoryOutlet = outletRepository.findByHardwareIdIs(outlet.getHardwareId());
        if (!repositoryOutlet.isPresent()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if (!repositoryOutlet.get().getConnected()){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        HubMessage message = new HubMessage(HubMessageType.OUTLET, outlet);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            messageService.sendMessage(hub.getHardwareId(), objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        eventClient.createEvent(hub.getGroupId(), repositoryOutlet.get().getName());

        repositoryOutlet.get().update(outlet);
        outletRepository.save(repositoryOutlet.get());

        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/service/{id}/outlet/{outletId}")
    public ResponseEntity<?> serviceUpdate(@PathVariable("id") Integer id, @PathVariable("outletId") String outletId, @RequestBody Outlet outlet){
        Hub hub = hubRepository.findByGroupId(id);
        if (!hub.isConnected()){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        Outlet repositoryOutlet = outletRepository.findOne(outletId);

        if (!repositoryOutlet.getConnected()){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        HubMessage message = new HubMessage(HubMessageType.OUTLET, outlet);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            messageService.sendMessage(hub.getHardwareId(), objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        eventClient.createEvent(hub.getGroupId(), repositoryOutlet.getName());

        repositoryOutlet.update(outlet);
        outletRepository.save(repositoryOutlet);

        return new ResponseEntity(HttpStatus.OK);
    }
}

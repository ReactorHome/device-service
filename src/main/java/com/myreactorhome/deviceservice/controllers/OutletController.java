package com.myreactorhome.deviceservice.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class OutletController {

    @Autowired
    OutletRepository outletRepository;

    @Autowired
    MessageService messageService;

    @Autowired
    HubRepository hubRepository;

    @PatchMapping("/api/{id}/outlet/{outletId}")
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

        repositoryOutlet.get().update(outlet);
        outletRepository.save(repositoryOutlet.get());

        return new ResponseEntity(HttpStatus.OK);
    }
}

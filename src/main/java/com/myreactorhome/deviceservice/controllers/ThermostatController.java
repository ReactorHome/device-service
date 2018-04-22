package com.myreactorhome.deviceservice.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myreactorhome.deviceservice.feign_clients.EventClient;
import com.myreactorhome.deviceservice.feign_clients.NestClient;
import com.myreactorhome.deviceservice.feign_clients.NestRegistrationClient;
import com.myreactorhome.deviceservice.models.Hub;
import com.myreactorhome.deviceservice.models.Thermostat;
import com.myreactorhome.deviceservice.repositories.HubRepository;
import com.myreactorhome.deviceservice.repositories.ThermostatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
public class ThermostatController {
    @Autowired
    private HubRepository hubRepository;

    @Autowired
    private ThermostatRepository thermostatRepository;

    @Autowired
    private NestClient nestClient;

    @Autowired
    private EventClient eventClient;

    @Autowired
    private NestRegistrationClient nestRegistrationClient;

    @Value("${reactor.nest.id}")
    private String nestClientId;

    @Value("${reactor.nest.secret}")
    private String nestClientSecret;

    @PostMapping("/api/{id}/thermostat")
    ResponseEntity<?> createThermostat(@PathVariable("id") String id, @RequestBody Thermostat thermostat){
        Hub hub = hubRepository.findOne(id);
        hub.getDevices().add(thermostat);
        hubRepository.save(hub);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/api/{id}/thermostat/register/nest")
    @PreAuthorize("isGroupMember(#id)")
    List<Thermostat> registerWithNest(@PathVariable("id") String id,@RequestBody String nestAuthToken){

        Map<String, String> formParams = new HashMap<>();
        formParams.put("client_id", nestClientId);
        formParams.put("client_secret", nestClientSecret);
        formParams.put("code", nestAuthToken);
        formParams.put("grant_type", "authorization_code");

        Map<String, Object> authResponse = nestRegistrationClient.register(formParams);
        //Map<String, Object> authResponse = nestRegistrationClient.register(nestClientId, nestClientSecret, nestAuthToken, "authorization_code");

        List<Thermostat> thermostats = new ArrayList<>();
        System.out.println(nestAuthToken);


        String jsonS = nestClient.getThermostats("Bearer " + (String)authResponse.get("access_token"));
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode json;
        try {
            json = mapper.readTree(jsonS);
            for (final JsonNode thermostat : json) {
                 Thermostat thermostat1 = mapper.treeToValue(thermostat, Thermostat.class);
                 thermostat1.setApiKey(nestAuthToken);
                 thermostats.add(thermostat1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Hub hub = hubRepository.findOne(id);
        if(hub.getDevices() == null){
            hub.setDevices(new HashSet<>());
        }
        thermostatRepository.save(thermostats);
        hub.getDevices().addAll(thermostats);
        hubRepository.save(hub);
        System.out.println(hub.getDevices());
        return thermostats;
    }

    @PatchMapping("/api/{hubId}/thermostat/{thermostatId}")
    @PreAuthorize("isGroupMember(#hubId)")
    ResponseEntity<?> updateThermostat(@PathVariable("hubId") String hubId, @PathVariable("thermostatId") String thermostatId, @RequestBody Thermostat thermostat){
        String nestToken = thermostatRepository.findOne(thermostatId).getApiKey();
        String nestDeviceId = thermostat.getDeviceId();
        thermostat.setDeviceId(null);
        nestClient.updateThermostat(nestToken, nestDeviceId, thermostat);

        eventClient.createEvent(hubRepository.findOne(hubId).getGroupId(), thermostat.getName());

        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/service/{id}/thermostat/{thermostatId}")
    ResponseEntity<?> serviceUpdate(@PathVariable("id") Integer id, @PathVariable("thermostatId") String thermostatId, @RequestBody Thermostat thermostat){
        String nestToken = thermostatRepository.findOne(thermostatId).getApiKey();
        String nestDeviceId = thermostat.getDeviceId();
        thermostat.setDeviceId(null);
        nestClient.updateThermostat(nestToken, nestDeviceId, thermostat);

        eventClient.createEvent(hubRepository.findByGroupId(id).getGroupId(), thermostat.getName());

        return new ResponseEntity(HttpStatus.OK);
    }
}

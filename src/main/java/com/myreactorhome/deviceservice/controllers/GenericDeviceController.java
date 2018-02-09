package com.myreactorhome.deviceservice.controllers;

import com.myreactorhome.deviceservice.models.*;
import com.myreactorhome.deviceservice.repositories.*;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GenericDeviceController {

    @Autowired
    private HubRepository hubRepository;

    @Autowired
    private LightRepository lightRepository;

    @Autowired
    private OutletRepository outletRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private ThermostatRepository thermostatRepository;


    @GetMapping ("api/device/{id}")
    public GenericDevice index(@PathVariable("id") String id) {

        if(lightRepository.exists(id)) {
            return lightRepository.findOne(id);
        }
        else if(outletRepository.exists(id)) {
            return outletRepository.findOne(id);
        }
        else if(sensorRepository.exists(id)) {
            return sensorRepository.findOne(id);
        }
        else if(thermostatRepository.exists(id)) {
            return thermostatRepository.findOne(id);
        }
        else {
            return null;
        }
    }

    @PostMapping("api/device/light/{id}")
    public ResponseEntity<?> createLight(@PathVariable("id") String id, @RequestBody Light light){
        Hub hub = hubRepository.findOne(id);
        hub.getDevices().add(light);
        hubRepository.save(hub);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("api/device/outlet/{id}")
    ResponseEntity<?> createLight(@PathVariable("id") String id, @RequestBody Outlet outlet){
        Hub hub = hubRepository.findOne(id);
        hub.getDevices().add(outlet);
        hubRepository.save(hub);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("api/device/sensor/{id}")
    ResponseEntity<?> createLight(@PathVariable("id") String id, @RequestBody Sensor sensor){
        Hub hub = hubRepository.findOne(id);
        hub.getDevices().add(sensor);
        hubRepository.save(hub);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("api/device/thermostat/{id}")
    ResponseEntity<?> createLight(@PathVariable("id") String id, @RequestBody Thermostat thermostat){
        Hub hub = hubRepository.findOne(id);
        hub.getDevices().add(thermostat);
        hubRepository.save(hub);

        return new ResponseEntity(HttpStatus.CREATED);
    }
}

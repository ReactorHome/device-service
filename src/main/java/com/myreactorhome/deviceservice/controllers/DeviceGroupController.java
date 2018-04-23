package com.myreactorhome.deviceservice.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myreactorhome.deviceservice.exceptions.ModelNotFound;
import com.myreactorhome.deviceservice.models.*;
import com.myreactorhome.deviceservice.repositories.DeviceGroupRepository;
import com.myreactorhome.deviceservice.repositories.HubRepository;
import com.myreactorhome.deviceservice.services.MessageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@RestController
public class DeviceGroupController {

    @Autowired
    private HubRepository hubRepository;

    @Autowired
    private DeviceGroupRepository deviceGroupRepository;

    @Autowired
    private MessageService messageService;

    @PostMapping("api/{id}/device-groups")
    @PreAuthorize("isGroupMember(#id)")
    ResponseEntity<?> create(@PathVariable("id") String id, @RequestBody DeviceGroup deviceGroup){
        Hub hub = hubRepository.findOne(id);

        if(deviceGroup.getDevices() == null){
            deviceGroup.setDevices(new HashSet<>());
        }else{
            GenericDevice[] deviceArray =deviceGroup.getDevices().toArray(new GenericDevice[0]);

            Set<DeviceCapabilities> capabilities = new HashSet<>(deviceArray[0].getType().capabilities);

            for(int i = 1; i < deviceArray.length; i++){
                capabilities.retainAll(deviceArray[i].getType().capabilities);
            }
            if(capabilities.isEmpty()){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            deviceGroup.setTypes(capabilities);

        }
        deviceGroupRepository.save(deviceGroup);

        if(hub.getDeviceGroups() == null){
            hub.setDeviceGroups(new HashSet<>());
        }

        hub.getDeviceGroups().add(deviceGroup);
        hubRepository.save(hub);

        return new ResponseEntity<>(deviceGroup.getId(),HttpStatus.CREATED);
    }

    @PatchMapping("/api/{id}/device-groups/{groupId}/devices")
    @PreAuthorize("isGroupMember(#id)")
    ResponseEntity<?> addDevices(@PathVariable("id") String id, @PathVariable("groupId") String groupId, @RequestBody Set<GenericDevice> devices){
        Optional<DeviceGroup> deviceGroupOptional = deviceGroupRepository.findById(groupId);
        DeviceGroup deviceGroup = deviceGroupOptional.orElseThrow(() -> new ModelNotFound("device group"));

        if(deviceGroup.getTypes() == null){
            deviceGroup.setTypes(new HashSet<>());
        }

        Set<DeviceCapabilities> capabilities = new HashSet<>();

        for (GenericDevice device : devices) {
            if(capabilities.isEmpty()){
                capabilities.addAll(device.getType().capabilities);
            }else{
                capabilities.retainAll(device.getType().capabilities);
            }

            if(!deviceGroup.getTypes().isEmpty()){
                capabilities.retainAll(deviceGroup.getTypes());
            }
            if (!deviceGroup.getTypes().isEmpty() && !capabilities.containsAll(deviceGroup.getTypes())) {
                return new ResponseEntity<>("Device with name " + device.getName() + " incompatible with group capabilities", HttpStatus.CONFLICT);
            }

            if(capabilities.isEmpty()){
                return new ResponseEntity<>("Device with name " + device.getName() + " incompatible with group capabilities", HttpStatus.CONFLICT);
            }

            if(!deviceGroup.getTypes().isEmpty()){
                capabilities.clear();
            }

        }

        if(deviceGroup.getTypes().isEmpty()){
            deviceGroup.setTypes(capabilities);
        }

        deviceGroup.getDevices().addAll(devices);
        deviceGroupRepository.save(deviceGroup);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/{id}/device-groups/{groupId}/devices")
    @PreAuthorize("isGroupMember(#id)")
    ResponseEntity<?> removeDevicesFromGroup(@PathVariable("id") String hubId, @PathVariable("groupId") String deviceGroupId, @RequestBody Set<GenericDevice> devices){
        Optional<DeviceGroup> deviceGroupOptional = deviceGroupRepository.findById(deviceGroupId);
        DeviceGroup deviceGroup = deviceGroupOptional.orElseThrow(() -> new ModelNotFound("device group"));

        deviceGroup.getDevices().removeAll(devices);
        deviceGroupRepository.save(deviceGroup);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/api/{id}/device-groups/{groupId}/state")
    @PreAuthorize("isGroupMember(#id)")
    ResponseEntity<?> update(@PathVariable("id") String id, @PathVariable("groupId") String groupId, @RequestBody Map<String, Object> params) throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        Hub hub = hubRepository.findOne(id);
        if (!hub.isConnected()){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        Optional<DeviceGroup> deviceGroupOptional = deviceGroupRepository.findById(groupId);
        DeviceGroup deviceGroup = deviceGroupOptional.orElseThrow(() -> new ModelNotFound("device group"));

        if(deviceGroup.getDevices() != null && !deviceGroup.getDevices().isEmpty()){
            for(String key: params.keySet()){
                for(GenericDevice device: deviceGroup.getDevices()){
                    HubMessageType type;
                    Light light;
                    Outlet outlet;
                    HubMessage message = new HubMessage();
                    if(device instanceof Light){
                        type = HubMessageType.LIGHT;
                        light = (Light) device;
                        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(key, Light.class);
                        propertyDescriptor.getWriteMethod().invoke(light, params.get(key));
                        message.setDevice(light);
                        message.setType(type);
                    }else{
                        type = HubMessageType.OUTLET;
                        outlet = (Outlet)device;
                        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(key, Outlet.class);
                        propertyDescriptor.getWriteMethod().invoke(outlet, params.get(key));
                        message.setDevice(outlet);
                        message.setType(type);
                    }

                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    try {
                        messageService.sendMessage(hub.getHardwareId(), objectMapper.writeValueAsString(message));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return new ResponseEntity<>(HttpStatus.OK);
    }



}

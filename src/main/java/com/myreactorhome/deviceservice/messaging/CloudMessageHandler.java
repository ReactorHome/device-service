package com.myreactorhome.deviceservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myreactorhome.deviceservice.feign_clients.EventClient;
import com.myreactorhome.deviceservice.models.GenericDevice;
import com.myreactorhome.deviceservice.models.Hub;
import com.myreactorhome.deviceservice.models.Light;
import com.myreactorhome.deviceservice.models.Outlet;
import com.myreactorhome.deviceservice.repositories.HubRepository;
import com.myreactorhome.deviceservice.repositories.LightRepository;
import com.myreactorhome.deviceservice.repositories.OutletRepository;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Optional;



public class CloudMessageHandler implements IMqttMessageListener{


    private ObjectMapper objectMapper;


    private OutletRepository outletRepository;


    private LightRepository lightRepository;


    private HubRepository hubRepository;


    private EventClient eventClient;


    public CloudMessageHandler(OutletRepository outletRepository, LightRepository lightRepository, HubRepository hubRepository, EventClient eventClient){
        this.objectMapper = new ObjectMapper();
        this.outletRepository = outletRepository;
        this.lightRepository = lightRepository;
        this.hubRepository = hubRepository;
        this.eventClient = eventClient;
    }


    @Override
    public void messageArrived(String s, MqttMessage mqttMessage){
        String message = null;
        try {
            message = new String(mqttMessage.getPayload(), "UTF-8");
            System.out.println(message);
            JsonNode root = objectMapper.readTree(message);

            String hardwareId = root.get("hardware_id").asText();
            Optional<Hub> hubOptional = hubRepository.findByHardwareId(hardwareId);
            Hub hub = null;
            if(hubOptional.isPresent()){
                hub = hubOptional.get();
                if(hub.getDevices() == null){
                    hub.setDevices(new HashSet<>());
                }
            }
            switch (root.get("type").asText()) {
                case "state_change":
                    this.handleDeviceConnections(root, hub);
                    break;
                case "connection":
                    this.handleHubStateChange(hardwareId, true);
                    break;
                case "disconnection":
                    this.handleHubStateChange(hardwareId, false);
                    break;

                case "bridge_message":
                    this.handleBridgeMessage(hub, root);
                    break;
            }
            if(hub != null){
                hubRepository.save(hub);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeviceConnections(JsonNode message, Hub hub){
        int connectionSize = message.get("state_change_list").size();

        for (int i = 0; i < connectionSize; i++){
            JsonNode currentDevice = message.get("state_change_list").get(i);
            try {
                this.createOrUpdateDevice(currentDevice, hub);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleHubStateChange(String hardware_id, boolean connected){
        System.out.println(hardware_id);
        Optional<Hub> hub = hubRepository.findByHardwareId(hardware_id);
        hub.ifPresent(hub1 -> {hub1.setConnected(connected); hubRepository.save(hub.get());});
    }

    private void createOrUpdateDevice(JsonNode message, Hub hub) throws JsonProcessingException {
        Integer groupId;

        switch (message.get("type").asInt()){
            case 0:
                Light light = objectMapper.treeToValue(message, Light.class);
                Optional<Light> lightOptional = lightRepository.findByHardwareIdIs(message.get("hardware_id").asText());
                lightOptional.ifPresent(light1 -> light.setId(light1.getId()));

                lightRepository.save(light);
                if(hub != null){
                    updateHubAndGenerateEvent(hub, light);
//                    groupId = hub.getGroupId();
//                    try{
//                        eventClient.createEvent(groupId, light.getId());
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//
//                    hub.getDevices().add(light);
                }
                break;

            case 1:
                Outlet outlet = objectMapper.treeToValue(message, Outlet.class);
                Optional<Outlet> outletOptional = outletRepository.findByHardwareIdIs(outlet.getHardwareId());
                outletOptional.ifPresent(outlet1 -> outlet.setId(outlet1.getId()));

                outletRepository.save(outlet);
                if(hub != null){
                    updateHubAndGenerateEvent(hub, outlet);
//                    groupId = hub.getGroupId();
//                    try{
//                        eventClient.createEvent(groupId, outlet.getId());
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    hub.getDevices().add(outlet);
                }
                break;
        }
    }

    private void handleBridgeMessage(Hub hub, JsonNode message){
        if(hub != null){
            int bridgeSize = message.get("data").size();

            for(int i = 0; i < bridgeSize; i++){
                JsonNode currentBridge = message.get("data").get(i);
                String bridgeText = null;
                try {
                    bridgeText = objectMapper.writeValueAsString(currentBridge);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                eventClient.createEvent(hub.getGroupId(), currentBridge.get("name").asText(), bridgeText);
            }

        }
    }

    private void updateHubAndGenerateEvent(Hub hub, GenericDevice device){
        if(hub != null){
            Integer groupId = hub.getGroupId();
            try{
                eventClient.createEvent(groupId, device.getId());
            }catch (Exception e){
                e.printStackTrace();
            }
            hub.getDevices().add(device);
        }
    }
}

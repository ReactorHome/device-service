package com.myreactorhome.deviceservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Optional;



public class CloudMessageHandler implements IMqttMessageListener {


    private ObjectMapper objectMapper;


    private OutletRepository outletRepository;


    private LightRepository lightRepository;


    private HubRepository hubRepository;


    public CloudMessageHandler(OutletRepository outletRepository, LightRepository lightRepository, HubRepository hubRepository){
        this.objectMapper = new ObjectMapper();
        this.outletRepository = outletRepository;
        this.lightRepository = lightRepository;
        this.hubRepository = hubRepository;
    }


    @Override
    public void messageArrived(String s, MqttMessage mqttMessage){
        String message = null;
        try {
            message = new String(mqttMessage.getPayload(), "UTF-8");
            JsonNode root = objectMapper.readTree(message);
            switch (root.get("message_type").asText()) {
                case "state_change":
                    this.handleDeviceConnections(root);
                    break;
                case "connection":
                    this.handleHubStateChange(root.get("hardware_id").asText(), true);
                    break;
                case "disconnection":
                    this.handleHubStateChange(root.get("hardware_id").asText(), false);
                    break;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeviceConnections(JsonNode message){
        int connectionSize = message.get("state_change_list").size();

        for (int i = 0; i < connectionSize; i++){
            JsonNode currentDevice = message.get("state_change_list").get(i);
            try {
                this.createOrUpdateDevice(currentDevice);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleHubStateChange(String hardware_id, boolean connected){
        Hub hub = hubRepository.findByHardwareId(hardware_id);
        hub.setConnected(connected);
        hubRepository.save(hub);
    }

    private void createOrUpdateDevice(JsonNode message) throws JsonProcessingException {
        switch (message.get("type").asInt()){
            case 0:
                Light light = objectMapper.treeToValue(message, Light.class);
                Optional<Light> lightOptional = lightRepository.findByHardwareIdIs(message.get("hardware_id").asText());
                lightOptional.ifPresent(light1 -> light.setId(light1.getId()));
                lightRepository.save(light);
                break;

            case 1:
                Outlet outlet = objectMapper.treeToValue(message, Outlet.class);
                Optional<Outlet> outletOptional = outletRepository.findByHardwareIdIs(outlet.getHardwareId());
                outletOptional.ifPresent(outlet1 -> outlet.setId(outlet1.getId()));

                outletRepository.save(outlet);
                break;
        }
    }
}

package com.myreactorhome.deviceservice.services;

import com.myreactorhome.deviceservice.feign_clients.EventClient;
import com.myreactorhome.deviceservice.messaging.CloudMessageHandler;
import com.myreactorhome.deviceservice.repositories.HubRepository;
import com.myreactorhome.deviceservice.repositories.LightRepository;
import com.myreactorhome.deviceservice.repositories.OutletRepository;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.internal.security.SSLSocketFactoryFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Configuration
@Component//implements DisposableBean
public class MessageService implements DisposableBean{

    public MqttClient mqttClient;

    final
    OutletRepository outletRepository;

    final
    LightRepository lightRepository;

    final
    HubRepository hubRepository;

    final EventClient eventClient;

    @Autowired
    public MessageService(OutletRepository outletRepository, LightRepository lightRepository, HubRepository hubRepository, @Value("${reactor.broker.uri}") String serverURI, EventClient eventClient){
        System.out.println("Server URI " + serverURI);
        try {
            this.mqttClient = new MqttClient(serverURI, "device-service2");
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setKeepAliveInterval(30);
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);
            if(serverURI.contains("ssl")){
                SSLSocketFactoryFactory factoryFactory = new SSLSocketFactoryFactory();
                connOpts.setSocketFactory(factoryFactory.createSocketFactory(null));
            }
            this.mqttClient.connect(connOpts);
            this.mqttClient.subscribe("cloud_messaging", new CloudMessageHandler(outletRepository, lightRepository, hubRepository, eventClient));
        } catch (MqttException e) {
            e.printStackTrace();
        }
        this.outletRepository = outletRepository;
        this.lightRepository = lightRepository;
        this.hubRepository = hubRepository;
        this.eventClient = eventClient;
    }

    public void sendMessage(String topic, String message){
        MqttMessage message1 = new MqttMessage();
        message1.setPayload(message.getBytes());
        message1.setQos(0);
        message1.setRetained(false);
        try {
            this.mqttClient.publish(topic, message1);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() throws Exception {
        if (this.mqttClient.isConnected()){
            this.mqttClient.disconnect();
        }
    }
}

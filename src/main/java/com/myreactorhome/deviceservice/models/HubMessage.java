package com.myreactorhome.deviceservice.models;

public class HubMessage {

    private HubMessageType type;
    private GenericDevice device;

    public HubMessage(){}

    public HubMessage(HubMessageType type, GenericDevice device) {
        this.type = type;
        this.device = device;
    }

    public HubMessageType getType() {
        return type;
    }

    public void setType(HubMessageType type) {
        this.type = type;
    }

    public GenericDevice getDevice() {
        return device;
    }

    public void setDevice(GenericDevice device) {
        this.device = device;
    }
}

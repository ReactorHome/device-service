package com.myreactorhome.deviceservice.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "hubs")
public class Hub {

    @Id
    private String id;
    private Integer groupId;

    @DBRef
    private List<GenericDevice> devices;

    private String hardwareId;

    private boolean connected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<GenericDevice> getDevices() {
        return devices;
    }

    public void setDevices(List<GenericDevice> devices) {
        this.devices = devices;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}

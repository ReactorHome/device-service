package com.myreactorhome.deviceservice.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Document(collection = "hubs")
public class Hub {

    @Id
    private String id;
    private Integer groupId;

    @DBRef
    private Set<GenericDevice> devices;

    @DBRef
    private Set<DeviceGroup> deviceGroups;

    private String hardwareId;

    private boolean connected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<GenericDevice> getDevices() {
        return devices;
    }

    public void setDevices(Set<GenericDevice> devices) {
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

    public Set<DeviceGroup> getDeviceGroups() {
        return deviceGroups;
    }

    public void setDeviceGroups(Set<DeviceGroup> deviceGroups) {
        this.deviceGroups = deviceGroups;
    }
}

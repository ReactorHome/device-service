package com.myreactorhome.deviceservice.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/*
    {
	"id": 0,
	"type": "GENERAL",
	"hardware_id": "01:01:01:01:01:01",
	"connected": true,
	"name": "",
	"manufacturer": "Philips",
	"connection_address": "192.168.1.214",
	"model": "LCT001"
}
 */

public abstract class GenericDevice {

    @Id
    private String id;

    protected DeviceType type;
    @Indexed
    private String hardwareId;
    private Boolean connected;
    private String name;
    private String manufacturer;
    private String connectionAddress;
    private String model;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardware_id) {
        this.hardwareId = hardware_id;
    }

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getConnectionAddress() {
        return connectionAddress;
    }

    public void setConnectionAddress(String connection_address) {
        this.connectionAddress = connection_address;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}

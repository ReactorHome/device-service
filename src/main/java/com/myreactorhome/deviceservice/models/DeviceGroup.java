package com.myreactorhome.deviceservice.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "device-groups")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DeviceGroup {
    @Id
    private String id;

    @DBRef
    private Set<GenericDevice> devices;

    private String name;

    private Set<DeviceCapabilities> types;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<DeviceCapabilities> getTypes() {
        return types;
    }

    public void setTypes(Set<DeviceCapabilities> types) {
        this.types = types;
    }
}

package com.myreactorhome.deviceservice.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Light.class, name = "0"),

        @JsonSubTypes.Type(value = Outlet.class, name = "1"),
        @JsonSubTypes.Type(value = Thermostat.class, name="2")
}
)
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

    @Override
    public boolean equals(Object obj) {
        if(getClass() == obj.getClass()){
            GenericDevice other = (GenericDevice) obj;
            return other.getHardwareId().equals(this.getHardwareId());
        }
        return false;

    }

    @Override
    public int hashCode() {
        return this.getHardwareId().hashCode();
    }
}

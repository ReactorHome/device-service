package com.myreactorhome.deviceservice.models;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.*;

import static com.myreactorhome.deviceservice.models.DeviceCapabilities.*;

public enum DeviceType {
    LIGHT(new DeviceCapabilities[]{ONOFF, BRIGHTNESS, HUE}),
    OUTLET(new DeviceCapabilities[]{ONOFF}),
    THERMOSTAT(new DeviceCapabilities[]{TEMPERATURE}),
    SENSOR(new DeviceCapabilities[]{DATA});

    public Set<DeviceCapabilities> capabilities;

    DeviceType(DeviceCapabilities[] capabilities){
        this.capabilities = new HashSet<>();
        this.capabilities.addAll(Arrays.asList(capabilities));
    }


    @JsonValue
    public int toValue() {
        return  ordinal();
    }
}

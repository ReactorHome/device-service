package com.myreactorhome.deviceservice.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DeviceType {
    LIGHT,
    OUTLET,
    THERMOSTAT,
    SENSOR;


    @JsonValue
    public int toValue() {
        return  ordinal();
    }
}

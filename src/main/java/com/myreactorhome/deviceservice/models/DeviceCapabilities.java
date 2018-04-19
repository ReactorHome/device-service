package com.myreactorhome.deviceservice.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DeviceCapabilities {
    ONOFF,
    BRIGHTNESS,
    TEMPERATURE,
    HUE,
    DATA;

    @JsonValue
    public int toValue() {
        return  ordinal();
    }
}

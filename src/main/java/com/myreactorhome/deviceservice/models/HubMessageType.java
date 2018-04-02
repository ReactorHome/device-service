package com.myreactorhome.deviceservice.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum HubMessageType {
    OUTLET,
    LIGHT;


    @JsonValue
    public int toValue() {
        return  ordinal();
    }
}

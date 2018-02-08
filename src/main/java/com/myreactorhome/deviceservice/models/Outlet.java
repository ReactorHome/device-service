package com.myreactorhome.deviceservice.models;

/*
{
	"id": 0,
	"type": "OUTLET",
	"hardware_id": "01:01:01:01:01:01",
	"connected": true,
	"name": "",
	"manufacturer": "TP-Link",
	"connection_address": "192.168.1.214",
	"model": "LCT001",
	"on": true
}
 */

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "outlets")
public class Outlet extends GenericDevice{
    private Boolean on;

    public Outlet(){
        this.setType(DeviceType.OUTLET);
    }

    public Boolean getOn() {
        return on;
    }

    public void setOn(Boolean on) {
        this.on = on;
    }
}

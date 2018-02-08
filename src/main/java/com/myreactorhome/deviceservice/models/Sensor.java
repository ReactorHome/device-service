package com.myreactorhome.deviceservice.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "sensors")
public class Sensor extends GenericDevice {

    private List<TimeSeriesData> data;

    public Sensor() {
        this.setType(DeviceType.SENSOR);
    }

    public List<TimeSeriesData> getData() {
        return data;
    }

    public void setData(List<TimeSeriesData> data) {
        this.data = data;
    }
}

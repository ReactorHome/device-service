package com.myreactorhome.deviceservice.models;

/*
{
	"id": 0,
	"type": "LIGHT",
	"device_id": "01:01:01:01:01:01",
	"connected": true,
	"name": "",
	"manufacturer": "Philips",
	"connection_address": "192.168.1.214",
	"model": "LCT001",
	"on": true,
	"brightness": 100,
	"supports_color": true,
	"color_mode": "hs",
	"hue": 28502,
	"saturation": 254,
	"xy": [0.5, 0.5],
	"color_temperature": 500
}
 */

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "lights")
public class Light extends GenericDevice {

    private Boolean on;
    private Integer brightness;
    private Boolean supports_color;
    private String color_mode;
    private Integer hue;
    private Integer saturation;
    private List<Integer> xy;
    private Integer color_temperature;

    public Light() {
        this.setType(DeviceType.LIGHT);
    }

    public Boolean getOn() {
        return on;
    }

    public void setOn(Boolean on) {
        this.on = on;
    }

    public Integer getBrightness() {
        return brightness;
    }

    public void setBrightness(Integer brightness) {
        this.brightness = brightness;
    }

    public Boolean getSupports_color() {
        return supports_color;
    }

    public void setSupports_color(Boolean supports_color) {
        this.supports_color = supports_color;
    }

    public String getColor_mode() {
        return color_mode;
    }

    public void setColor_mode(String color_mode) {
        this.color_mode = color_mode;
    }

    public Integer getHue() {
        return hue;
    }

    public void setHue(Integer hue) {
        this.hue = hue;
    }

    public Integer getSaturation() {
        return saturation;
    }

    public void setSaturation(Integer saturation) {
        this.saturation = saturation;
    }

    public List<Integer> getXy() {
        return xy;
    }

    public void setXy(List<Integer> xy) {
        this.xy = xy;
    }

    public Integer getColor_temperature() {
        return color_temperature;
    }

    public void setColor_temperature(Integer color_temperature) {
        this.color_temperature = color_temperature;
    }
}

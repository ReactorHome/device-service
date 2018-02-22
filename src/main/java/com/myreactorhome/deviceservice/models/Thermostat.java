package com.myreactorhome.deviceservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Thermostat extends GenericDevice {

    private String nestDeviceId;
    private Boolean canCool;
    private Boolean canHeat;
    private Boolean isUsingEmergencyHeat;
    private Boolean hasFan;
    // R/W
    private Boolean fanTimerActive;
    // End R/W
    private Boolean hasLeaf;

    //R/W
    private String temperatureScale;
    private Integer targetTemperatureF;
    private Integer targetTemperatureC;
    private Integer targetTemperatureHighF;
    private Integer targetTemperatureHighC;
    private Integer targetTemperatureLowF;
    private Integer targetTemperatureLowC;
    //End R/W
    private Integer ecoTemperatureHighF;
    private Integer ecoTemperatureHighC;
    private Integer ecoTemperatureLowF;
    private Integer ecoTemperatureLowC;
    private String previousHvacMode;
    // R/W
    private String label;
    private String hvacMode;
    private Integer fanTimerDuration;
    // End R/W


    public Thermostat() {
        this.setType(DeviceType.THERMOSTAT);
    }

    public String getNestDeviceId() {
        return nestDeviceId;
    }

    public void setNestDeviceId(String nestDeviceId) {
        this.nestDeviceId = nestDeviceId;
    }

    public Boolean getCanCool() {
        return canCool;
    }

    public void setCanCool(Boolean canCool) {
        this.canCool = canCool;
    }

    public Boolean getCanHeat() {
        return canHeat;
    }

    public void setCanHeat(Boolean canHeat) {
        this.canHeat = canHeat;
    }

    public Boolean getUsingEmergencyHeat() {
        return isUsingEmergencyHeat;
    }

    public void setUsingEmergencyHeat(Boolean usingEmergencyHeat) {
        isUsingEmergencyHeat = usingEmergencyHeat;
    }

    public Boolean getHasFan() {
        return hasFan;
    }

    public void setHasFan(Boolean hasFan) {
        this.hasFan = hasFan;
    }

    public Boolean getFanTimerActive() {
        return fanTimerActive;
    }

    public void setFanTimerActive(Boolean fanTimerActive) {
        this.fanTimerActive = fanTimerActive;
    }

    public Boolean getHasLeaf() {
        return hasLeaf;
    }

    public void setHasLeaf(Boolean hasLeaf) {
        this.hasLeaf = hasLeaf;
    }

    public String getTemperatureScale() {
        return temperatureScale;
    }

    public void setTemperatureScale(String temperatureScale) {
        this.temperatureScale = temperatureScale;
    }

    public Integer getTargetTemperatureF() {
        return targetTemperatureF;
    }

    public void setTargetTemperatureF(Integer targetTemperatureF) {
        this.targetTemperatureF = targetTemperatureF;
    }

    public Integer getTargetTemperatureC() {
        return targetTemperatureC;
    }

    public void setTargetTemperatureC(Integer targetTemperatureC) {
        this.targetTemperatureC = targetTemperatureC;
    }

    public Integer getTargetTemperatureHighF() {
        return targetTemperatureHighF;
    }

    public void setTargetTemperatureHighF(Integer targetTemperatureHighF) {
        this.targetTemperatureHighF = targetTemperatureHighF;
    }

    public Integer getTargetTemperatureHighC() {
        return targetTemperatureHighC;
    }

    public void setTargetTemperatureHighC(Integer targetTemperatureHighC) {
        this.targetTemperatureHighC = targetTemperatureHighC;
    }

    public Integer getTargetTemperatureLowF() {
        return targetTemperatureLowF;
    }

    public void setTargetTemperatureLowF(Integer targetTemperatureLowF) {
        this.targetTemperatureLowF = targetTemperatureLowF;
    }

    public Integer getTargetTemperatureLowC() {
        return targetTemperatureLowC;
    }

    public void setTargetTemperatureLowC(Integer targetTemperatureLowC) {
        this.targetTemperatureLowC = targetTemperatureLowC;
    }

    public Integer getEcoTemperatureHighF() {
        return ecoTemperatureHighF;
    }

    public void setEcoTemperatureHighF(Integer ecoTemperatureHighF) {
        this.ecoTemperatureHighF = ecoTemperatureHighF;
    }

    public Integer getEcoTemperatureHighC() {
        return ecoTemperatureHighC;
    }

    public void setEcoTemperatureHighC(Integer ecoTemperatureHighC) {
        this.ecoTemperatureHighC = ecoTemperatureHighC;
    }

    public Integer getEcoTemperatureLowF() {
        return ecoTemperatureLowF;
    }

    public void setEcoTemperatureLowF(Integer ecoTemperatureLowF) {
        this.ecoTemperatureLowF = ecoTemperatureLowF;
    }

    public Integer getEcoTemperatureLowC() {
        return ecoTemperatureLowC;
    }

    public void setEcoTemperatureLowC(Integer ecoTemperatureLowC) {
        this.ecoTemperatureLowC = ecoTemperatureLowC;
    }

    public String getHvacMode() {
        return hvacMode;
    }

    public void setHvacMode(String hvacMode) {
        this.hvacMode = hvacMode;
    }

    public String getPreviousHvacMode() {
        return previousHvacMode;
    }

    public void setPreviousHvacMode(String previousHvacMode) {
        this.previousHvacMode = previousHvacMode;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getFanTimerDuration() {
        return fanTimerDuration;
    }

    public void setFanTimerDuration(Integer fanTimerDuration) {
        this.fanTimerDuration = fanTimerDuration;
    }
}

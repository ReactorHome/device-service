package com.myreactorhome.deviceservice.feign_clients;

import com.myreactorhome.deviceservice.models.Thermostat;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "nest", url = "https://developer-api.nest.com")
public interface NestClient {
    @RequestMapping(method = RequestMethod.GET, path = "/devices/thermostats")
    String getThermostats(@RequestHeader("Authorization") String token);

    @RequestMapping(method = RequestMethod.PUT, path = "/devices/thermostats/{device_id}")
    void updateThermostat(@RequestHeader("Authorization") String token, @PathVariable("device_id") String deviceId, @RequestBody Thermostat thermostat);
}

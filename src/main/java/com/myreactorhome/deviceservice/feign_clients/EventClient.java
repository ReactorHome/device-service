package com.myreactorhome.deviceservice.feign_clients;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value="event", url="${reactor.user-service}", configuration = EventClientConfiguration.class)
public interface EventClient {

    @PostMapping("service/events/{id}/{device}")
    ResponseEntity createEvent(@PathVariable("id") Integer groupId, @PathVariable("device") String device);

    @PostMapping("service/events/{id}/{device}")
    ResponseEntity createEvent(@PathVariable("id") Integer groupId, @PathVariable("device") String device, @RequestBody String json);
}
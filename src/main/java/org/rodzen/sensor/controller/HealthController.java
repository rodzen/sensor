package org.rodzen.sensor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/healthcheck")
    public void healthcheck() {
    }
}

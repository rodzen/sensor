package org.rodzen.sensor.controller;

import io.swagger.annotations.ApiOperation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.rodzen.sensor.dto.SensorAlertDto;
import org.rodzen.sensor.dto.SensorMeasurementDto;
import org.rodzen.sensor.dto.SensorMetricsDto;
import org.rodzen.sensor.dto.SensorStatusDto;
import org.rodzen.sensor.pubsub.SensorMeasurementProducer;
import org.rodzen.sensor.service.SensorService;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/sensors/")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SensorController {

    private final SensorService service;
    private final SensorMeasurementProducer producer;

    @PostMapping("/{uuid}/measurements")
    @ApiOperation(value = "Collects measurements for a given sensor")
    public void collect(@PathVariable @NonNull String uuid, @RequestBody SensorMeasurementDto measurement) {
        producer.send(uuid, measurement);
    }

    @GetMapping("/{uuid}")
    @ApiOperation(value = "Returns current status for a given sensor")
    public SensorStatusDto status(@PathVariable @NonNull String uuid) {
        return service.status(uuid);
    }

    @GetMapping("/{uuid}/metrics")
    @ApiOperation(value = "Returns metrics for a given sensor")
    public SensorMetricsDto metrics(@PathVariable @NonNull String uuid) {
        return service.metrics(uuid);
    }

    @GetMapping("/{uuid}/alerts")
    @ApiOperation(value = "Returns alerts for a given sensor")
    public List<SensorAlertDto> alerts(@PathVariable @NonNull String uuid) {
        return service.alerts(uuid);
    }
}

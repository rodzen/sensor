package org.rodzen.sensor.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SensorMetrics {

    private Integer maxLast30days;
    private Integer avgLast30days;
}

package org.rodzen.sensor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SensorMetricsDto {

    private Integer maxLast30days;
    private Integer avgLast30days;
}

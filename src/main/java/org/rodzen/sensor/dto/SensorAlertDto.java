package org.rodzen.sensor.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SensorAlertDto {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer measurement1;
    private Integer measurement2;
    private Integer measurement3;
}

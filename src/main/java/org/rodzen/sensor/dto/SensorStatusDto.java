package org.rodzen.sensor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.rodzen.sensor.model.SensorStatus;

@Data
@AllArgsConstructor
public class SensorStatusDto {

    private SensorStatus status;
}

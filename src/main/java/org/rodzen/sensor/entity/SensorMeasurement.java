package org.rodzen.sensor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Builder
@Entity(name = "measurements")
@NoArgsConstructor
@AllArgsConstructor
public class SensorMeasurement {

    @Id
    @GeneratedValue
    private Long id;
    private String uuid;
    private Integer co2;
    private LocalDateTime time;
}

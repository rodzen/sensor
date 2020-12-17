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
@Entity(name = "alerts")
@NoArgsConstructor
@AllArgsConstructor
public class SensorAlert {

    @Id
    @GeneratedValue
    private Long id;
    private String uuid;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer measurement1;
    private Integer measurement2;
    private Integer measurement3;
}

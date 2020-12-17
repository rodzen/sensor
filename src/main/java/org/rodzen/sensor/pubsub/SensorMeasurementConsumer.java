package org.rodzen.sensor.pubsub;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rodzen.sensor.entity.SensorMeasurement;
import org.rodzen.sensor.repository.SensorMeasurementRepository;
import org.rodzen.sensor.service.AlertService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SensorMeasurementConsumer {

    private final AlertService alertService;

    private final SensorMeasurementRepository measurementRepository;

    @KafkaListener(topics = "measurements", groupId = "simple-group")
    public void listen(SensorMeasurementMessage measurement) {
        log.info("Received Measurement: " + measurement);
        store(measurement);
        alertService.handle(measurement);
    }

    private void store(SensorMeasurementMessage measurement) {
        measurementRepository.save(SensorMeasurement.builder()
                                           .uuid(measurement.getUuid())
                                           .co2(measurement.getCo2())
                                           .time(measurement.getTime())
                                           .build());
    }
}

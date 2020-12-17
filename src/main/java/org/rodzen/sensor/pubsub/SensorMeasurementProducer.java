package org.rodzen.sensor.pubsub;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.rodzen.sensor.dto.SensorMeasurementDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SensorMeasurementProducer {

    private KafkaTemplate<String, SensorMeasurementMessage> kafkaTemplate;

    public void send(String uuid, SensorMeasurementDto measurement) {
        kafkaTemplate.send("measurements", new SensorMeasurementMessage(uuid, measurement.getCo2(), measurement.getTime()));
    }
}

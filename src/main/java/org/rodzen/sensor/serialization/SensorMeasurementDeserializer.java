package org.rodzen.sensor.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.rodzen.sensor.pubsub.SensorMeasurementMessage;

import java.io.IOException;

@Slf4j
public class SensorMeasurementDeserializer implements Deserializer<SensorMeasurementMessage> {

    @Override
    public SensorMeasurementMessage deserialize(String s, byte[] bytes) {
        SensorMeasurementMessage result = null;
        try {
            result = new ObjectMapper().readValue(bytes, SensorMeasurementMessage.class);
        } catch (IOException ex) {
            log.error("Could not deserialize message", ex);
        }
        return result;
    }
}

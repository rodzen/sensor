package org.rodzen.sensor.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
import org.rodzen.sensor.pubsub.SensorMeasurementMessage;

@Slf4j
public class SensorMeasurementSerializer implements Serializer<SensorMeasurementMessage> {

    @Override
    public byte[] serialize(String s, SensorMeasurementMessage message) {
        byte[] result = null;
        try {
            result = new ObjectMapper().writeValueAsBytes(message);
        } catch (JsonProcessingException ex) {
            log.error("Could not serialize message", ex);
        }
        return result;
    }
}

package org.rodzen.sensor.handler;

import org.rodzen.sensor.entity.SensorMeasurement;
import org.rodzen.sensor.model.SensorStatus;
import org.rodzen.sensor.pubsub.SensorMeasurementMessage;

import java.util.List;

public interface AlertHandler {

    void handle(SensorMeasurementMessage measurement, List<SensorMeasurement> recentMeasurements);

    boolean appliesTo(boolean alertOngoing, SensorStatus status);
}

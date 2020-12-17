package org.rodzen.sensor.handler;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rodzen.sensor.entity.SensorMeasurement;
import org.rodzen.sensor.model.SensorStatus;
import org.rodzen.sensor.pubsub.SensorMeasurementMessage;
import org.rodzen.sensor.repository.SensorAlertRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RevokeAlertHandler implements AlertHandler {

    private final SensorAlertRepository alertRepository;

    @Override
    public void handle(SensorMeasurementMessage measurement, List<SensorMeasurement> recentMeasurements) {
        var uuid = measurement.getUuid();
        log.info("Revoking Alert for sensor: " + uuid);
        alertRepository.findMostRecentByUuid(uuid).ifPresent(alert -> {
            alert.setEndTime(measurement.getTime());
            alertRepository.save(alert);
        });
    }

    @Override
    public boolean appliesTo(boolean alertOngoing, SensorStatus status) {
        return alertOngoing && SensorStatus.OK == status;
    }
}

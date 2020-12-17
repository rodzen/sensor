package org.rodzen.sensor.handler;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rodzen.sensor.entity.SensorAlert;
import org.rodzen.sensor.entity.SensorMeasurement;
import org.rodzen.sensor.model.SensorStatus;
import org.rodzen.sensor.pubsub.SensorMeasurementMessage;
import org.rodzen.sensor.repository.SensorAlertRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RaiseAlertHandler implements AlertHandler {

    private final SensorAlertRepository alertRepository;

    @Override
    public void handle(SensorMeasurementMessage measurement, List<SensorMeasurement> recentMeasurements) {
        var uuid = measurement.getUuid();
        log.info("Storing Alert for sensor: " + uuid);
        alertRepository.save(SensorAlert.builder()
                                     .uuid(uuid)
                                     .startTime(recentMeasurements.get(0).getTime())
                                     .measurement1(recentMeasurements.get(2).getCo2())
                                     .measurement2(recentMeasurements.get(1).getCo2())
                                     .measurement3(recentMeasurements.get(0).getCo2())
                                     .build());
    }

    @Override
    public boolean appliesTo(boolean alertOngoing, SensorStatus status) {
        return !alertOngoing && SensorStatus.ALERT == status;
    }
}

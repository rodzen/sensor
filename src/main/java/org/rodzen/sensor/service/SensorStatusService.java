package org.rodzen.sensor.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.rodzen.sensor.entity.SensorMeasurement;
import org.rodzen.sensor.model.SensorStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SensorStatusService {

    private static final int THRESHOLD = 2000;

    public SensorStatus calculateStatus(List<SensorMeasurement> measurements, boolean alertOngoing) {
        var exceededCount = measurements.stream().filter(m -> m.getCo2() > THRESHOLD).count();
        if (exceededCount == 0 || !alertOngoing && measurements.get(0).getCo2() <= THRESHOLD) {
            return SensorStatus.OK;
        }
        if (alertOngoing || exceededCount == 3) {
            return SensorStatus.ALERT;
        }
        return SensorStatus.WARN;
    }
}

package org.rodzen.sensor.service;

import lombok.AllArgsConstructor;
import org.rodzen.sensor.handler.AlertHandler;
import org.rodzen.sensor.pubsub.SensorMeasurementMessage;
import org.rodzen.sensor.repository.SensorAlertRepository;
import org.rodzen.sensor.repository.SensorMeasurementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AlertService {

    private final List<AlertHandler> alertHandlers;

    private final SensorStatusService statusService;

    private final SensorAlertRepository alertRepository;
    private final SensorMeasurementRepository measurementRepository;

    public boolean isAlertOngoing(String uuid) {
        return alertRepository.findMostRecentByUuid(uuid)
                .filter(alert -> Objects.isNull(alert.getEndTime()))
                .isPresent();
    }

    public void handle(SensorMeasurementMessage measurement) {
        var uuid = measurement.getUuid();
        var alertOngoing = isAlertOngoing(uuid);
        var measurements = measurementRepository.findMostRecentByUuid(uuid);
        var status = statusService.calculateStatus(measurements, alertOngoing);

        alertHandlers.stream()
                .filter(handler -> handler.appliesTo(alertOngoing, status))
                .forEach(handler -> handler.handle(measurement, measurements));
    }
}

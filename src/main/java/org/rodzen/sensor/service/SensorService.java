package org.rodzen.sensor.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.rodzen.sensor.dto.SensorAlertDto;
import org.rodzen.sensor.dto.SensorMetricsDto;
import org.rodzen.sensor.dto.SensorStatusDto;
import org.rodzen.sensor.repository.SensorAlertRepository;
import org.rodzen.sensor.repository.SensorMeasurementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SensorService {

    private final SensorStatusService statusService;
    private final AlertService alertService;

    private final SensorMeasurementRepository measurementRepository;
    private final SensorAlertRepository alertRepository;

    public SensorStatusDto status(String uuid) {
        var measurements = measurementRepository.findMostRecentByUuid(uuid);
        var alertOngoing = alertService.isAlertOngoing(uuid);
        var status = statusService.calculateStatus(measurements, alertOngoing);
        return new SensorStatusDto(status);
    }

    public SensorMetricsDto metrics(String uuid) {
        return new SensorMetricsDto(measurementRepository.findMaxByUuid(uuid),
                                    measurementRepository.findAvgByUuid(uuid));
    }

    public List<SensorAlertDto> alerts(String uuid) {
        return alertRepository.findByUuid(uuid).stream()
                .map(alert -> SensorAlertDto.builder()
                        .startTime(alert.getStartTime())
                        .endTime(alert.getEndTime())
                        .measurement1(alert.getMeasurement1())
                        .measurement2(alert.getMeasurement2())
                        .measurement3(alert.getMeasurement3())
                        .build())
                .collect(Collectors.toList());
    }
}

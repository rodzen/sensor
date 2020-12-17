package org.rodzen.sensor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rodzen.sensor.controller.SensorController;
import org.rodzen.sensor.dto.SensorAlertDto;
import org.rodzen.sensor.dto.SensorMeasurementDto;
import org.rodzen.sensor.dto.SensorMetricsDto;
import org.rodzen.sensor.model.SensorStatus;
import org.rodzen.sensor.repository.SensorAlertRepository;
import org.rodzen.sensor.repository.SensorMeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static java.lang.Thread.sleep;
import static java.time.LocalDateTime.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9093", "port=9093"})
class SensorApplicationTests {

    private static final String UUID = "sample-uuid";
    private static final int INTERVAL = 100;

    @Autowired
    SensorMeasurementRepository measurementRepository;

    @Autowired
    SensorAlertRepository alertRepository;

    @Autowired
    SensorController sensorController;

    @BeforeEach
    void setUp() {
        measurementRepository.deleteAll();
        alertRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void shouldNotAcceptMeasurementWithEmptyData() {
        assertThrows(NullPointerException.class, () -> collect(UUID, null, "2020-12-20T12:12:12"));
        assertThrows(NullPointerException.class, () -> collect(UUID, 1000, (LocalDateTime) null));
    }

    @Test
    void shouldRaiseAndRevokeAlert() throws InterruptedException {

        // collect measurements in range
        collect(UUID, 1000, "2020-12-20T12:00:00");
        collect(UUID, 1000, "2020-12-20T12:01:00");
        collect(UUID, 1000, "2020-12-20T12:02:00");
        sleep(INTERVAL);

        // verify that sensor status is OK
        assertThat(sensorController.status(UUID).getStatus()).isEqualTo(SensorStatus.OK);

        // collect a measurement exceeding threshold
        collect(UUID, 3000, "2020-12-20T12:03:00");
        sleep(INTERVAL);

        // verify that sensor status is WARN
        assertThat(sensorController.status(UUID).getStatus()).isEqualTo(SensorStatus.WARN);

        // collect more measurements exceeding threshold
        collect(UUID, 3000, "2020-12-20T12:04:00");
        collect(UUID, 3000, "2020-12-20T12:05:00");
        sleep(INTERVAL);

        // verify that sensor status is ALERT
        assertThat(sensorController.status(UUID).getStatus()).isEqualTo(SensorStatus.ALERT);

        // verify that an alert was raised
        assertThat(StreamSupport.stream(alertRepository.findAll().spliterator(), false)
                           .filter(alert -> UUID.equals(alert.getUuid()))
                           .filter(alert -> parse("2020-12-20T12:05:00").equals(alert.getStartTime()))
                           .filter(alert -> Objects.isNull(alert.getEndTime()))
                           .count()).isEqualTo(1);

        // verify that an alert is returned by controller
        List<SensorAlertDto> alerts = sensorController.alerts(UUID);
        assertThat(alerts.size()).isEqualTo(1);
        assertThat(alerts.stream()
                           .filter(alert -> parse("2020-12-20T12:05:00").equals(alert.getStartTime()))
                           .filter(alert -> Objects.isNull(alert.getEndTime()))
                           .count()).isEqualTo(1);

        // collect 1st measurement in range
        collect(UUID, 1000, "2020-12-20T12:06:00");
        sleep(INTERVAL);

        // verify that sensor status is still ALERT
        assertThat(sensorController.status(UUID).getStatus()).isEqualTo(SensorStatus.ALERT);

        // collect 2nd measurement in range
        collect(UUID, 1000, "2020-12-20T12:07:00");
        sleep(INTERVAL);

        // verify that sensor status is still ALERT
        assertThat(sensorController.status(UUID).getStatus()).isEqualTo(SensorStatus.ALERT);

        // collect 3rd measurement in range
        collect(UUID, 1000, "2020-12-20T12:08:00");
        sleep(INTERVAL);

        // verify that sensor status is OK again after 3 consecutive measurements in range
        assertThat(sensorController.status(UUID).getStatus()).isEqualTo(SensorStatus.OK);

        // verify that alert was closed
        assertThat(StreamSupport.stream(alertRepository.findAll().spliterator(), false)
                           .filter(alert -> UUID.equals(alert.getUuid()))
                           .filter(alert -> parse("2020-12-20T12:05:00").equals(alert.getStartTime()))
                           .filter(alert -> parse("2020-12-20T12:08:00").equals(alert.getEndTime()))
                           .count()).isEqualTo(1);

        // verify that closed alert is returned by controller
        // verify that an alert is returned by controller
        alerts = sensorController.alerts(UUID);
        assertThat(alerts.size()).isEqualTo(1);
        assertThat(alerts.stream()
                           .filter(alert -> parse("2020-12-20T12:05:00").equals(alert.getStartTime()))
                           .filter(alert -> parse("2020-12-20T12:08:00").equals(alert.getEndTime()))
                           .count()).isEqualTo(1);

        // verify that all the measurements were saved
        assertThat(StreamSupport.stream(measurementRepository.findAll().spliterator(), false)
                           .filter(measurement -> UUID.equals(measurement.getUuid()))
                           .count()).isEqualTo(9);
    }

    @Test
    void shouldProvideMetricsForLast30Days() throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();

        // collect an old measurement
        collect(UUID, 1900, now.minusDays(60));

        // collect measurements from last 30 days
        collect(UUID, 1800, now.minusDays(9));
        collect(UUID, 1700, now.minusDays(6));
        collect(UUID, 1600, now.minusDays(3));

        sleep(INTERVAL);

        // verify that metrics returned are based on data from lat 30 days
        SensorMetricsDto metrics = sensorController.metrics(UUID);
        assertThat(metrics.getMaxLast30days()).isEqualTo(1800);
        assertThat(metrics.getAvgLast30days()).isEqualTo(1700);
    }

    private void collect(String uuid, Integer co2, String time) {
        collect(uuid, co2, parse(time));
    }

    private void collect(String uuid, Integer co2, LocalDateTime time) {
        sensorController.collect(uuid, new SensorMeasurementDto(co2, time));
    }
}

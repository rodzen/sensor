package org.rodzen.sensor.service

import org.rodzen.sensor.entity.SensorAlert
import org.rodzen.sensor.repository.SensorAlertRepository
import org.rodzen.sensor.repository.SensorMeasurementRepository
import spock.lang.Specification

import java.time.LocalDateTime

class AlertServiceTest extends Specification {

    def alertHandlers = []
    def statusService = Mock(SensorStatusService)
    def alertRepository = Mock(SensorAlertRepository)
    def measurementRepository = Mock(SensorMeasurementRepository)

    def service = new AlertService(alertHandlers, statusService, alertRepository, measurementRepository)

    def "should return false when alert not present"() {

        given:
        alertRepository.findMostRecentByUuid(_ as String) >> Optional.empty()

        when:
        def result = service.isAlertOngoing(_ as String)

        then:
        !result
    }

    def "should return true when alert present and not ended"() {

        given:
        alertRepository.findMostRecentByUuid(_ as String) >> Optional.of(
                SensorAlert.builder()
                        .startTime(LocalDateTime.parse("2020-12-20T12:12:12"))
                        .build()
        )

        when:
        def result = service.isAlertOngoing(_ as String)

        then:
        result
    }

    def "should return false when alert present and ended"() {

        given:
        alertRepository.findMostRecentByUuid(_ as String) >> Optional.of(
                SensorAlert.builder()
                        .startTime(LocalDateTime.parse("2020-12-20T12:12:12"))
                        .endTime(LocalDateTime.parse("2020-12-20T20:20:20"))
                        .build()
        )

        when:
        def result = service.isAlertOngoing(_ as String)

        then:
        !result
    }
}

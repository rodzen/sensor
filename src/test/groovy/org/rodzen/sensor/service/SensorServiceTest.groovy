package org.rodzen.sensor.service

import org.rodzen.sensor.entity.SensorAlert
import org.rodzen.sensor.model.SensorStatus
import org.rodzen.sensor.repository.SensorAlertRepository
import org.rodzen.sensor.repository.SensorMeasurementRepository
import spock.lang.Specification

class SensorServiceTest extends Specification {

    def statusService = Mock(SensorStatusService)
    def alertService = Mock(AlertService)
    def measurementRepository = Mock(SensorMeasurementRepository)
    def alertRepository = Mock(SensorAlertRepository)

    def service = new SensorService(statusService, alertService, measurementRepository, alertRepository)

    def "should calculate status for given sensor"() {

        given:
        def measurements = []
        def alertOngoing = true
        def status = SensorStatus.OK

        when:
        def result = service.status("uuid")

        then:
        1 * measurementRepository.findMostRecentByUuid("uuid") >> measurements
        1 * alertService.isAlertOngoing("uuid") >> alertOngoing
        1 * statusService.calculateStatus(measurements, alertOngoing) >> status
        result.getStatus() == status
    }

    def "should return alerts for given sensor"() {

        given:
        def sensorAlert = Mock(SensorAlert)
        sensorAlert.getUuid() >> "uuid"

        when:
        def result = service.alerts("uuid")

        then:
        1 * alertRepository.findByUuid("uuid") >> [sensorAlert]
        result.size() == 1
    }

    def "should return metrics for given sensor"() {

        when:
        def result = service.metrics("uuid")

        then:
        1 * measurementRepository.findMaxByUuid("uuid") >> 3000
        1 * measurementRepository.findAvgByUuid("uuid") >> 1000
        result.maxLast30days == 3000
        result.avgLast30days == 1000
    }
}

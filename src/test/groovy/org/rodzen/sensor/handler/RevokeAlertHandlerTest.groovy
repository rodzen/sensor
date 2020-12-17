package org.rodzen.sensor.handler

import org.rodzen.sensor.entity.SensorAlert
import org.rodzen.sensor.model.SensorStatus
import org.rodzen.sensor.pubsub.SensorMeasurementMessage
import org.rodzen.sensor.repository.SensorAlertRepository
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

class RevokeAlertHandlerTest extends Specification {

    def alertRepository = Mock(SensorAlertRepository)
    def message = Mock(SensorMeasurementMessage)
    def sensorAlert = Mock(SensorAlert)

    def handler = new RevokeAlertHandler(alertRepository)

    @Unroll
    def "should return [#result] for [alertOngoing=#alertOngoing] and [status=#status]"() {

        expect:
        result == handler.appliesTo(alertOngoing, status)

        where:
        result | alertOngoing | status
        true   | true         | SensorStatus.OK
        false  | true         | SensorStatus.WARN
        false  | true         | SensorStatus.ALERT
        false  | false        | SensorStatus.OK
        false  | false        | SensorStatus.WARN
        false  | false        | SensorStatus.ALERT
    }

    def "should set end time for the ongoing alert"() {

        given:
        message.getUuid() >> "uuid"
        message.getTime() >> LocalDateTime.parse("2020-12-20T12:12:12")

        when:
        handler.handle(message, [])

        then:
        1 * alertRepository.findMostRecentByUuid("uuid") >> Optional.of(sensorAlert)
        1 * sensorAlert.setEndTime(LocalDateTime.parse("2020-12-20T12:12:12"))
        1 * alertRepository.save(sensorAlert)
    }
}

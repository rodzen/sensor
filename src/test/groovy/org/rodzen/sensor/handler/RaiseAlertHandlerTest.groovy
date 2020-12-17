package org.rodzen.sensor.handler

import org.rodzen.sensor.entity.SensorAlert
import org.rodzen.sensor.entity.SensorMeasurement
import org.rodzen.sensor.model.SensorStatus
import org.rodzen.sensor.pubsub.SensorMeasurementMessage
import org.rodzen.sensor.repository.SensorAlertRepository
import spock.lang.Specification
import spock.lang.Unroll

class RaiseAlertHandlerTest extends Specification {

    def alertRepository = Mock(SensorAlertRepository)

    def handler = new RaiseAlertHandler(alertRepository)

    @Unroll
    def "should return [#result] for [alertOngoing=#alertOngoing] and [status=#status]"() {

        expect:
        result == handler.appliesTo(alertOngoing, status)

        where:
        result | alertOngoing | status
        false  | true         | SensorStatus.OK
        false  | true         | SensorStatus.WARN
        false  | true         | SensorStatus.ALERT
        false  | false        | SensorStatus.OK
        false  | false        | SensorStatus.WARN
        true   | false        | SensorStatus.ALERT
    }

    def "should call repository to store the alert"() {

        when:
        handler.handle(new SensorMeasurementMessage(), [new SensorMeasurement(), new SensorMeasurement(), new SensorMeasurement()])

        then:
        1 * alertRepository.save(_ as SensorAlert)
    }
}

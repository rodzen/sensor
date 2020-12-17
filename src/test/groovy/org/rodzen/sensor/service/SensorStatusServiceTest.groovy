package org.rodzen.sensor.service


import org.rodzen.sensor.entity.SensorMeasurement
import org.rodzen.sensor.model.SensorStatus
import spock.lang.Specification
import spock.lang.Unroll

class SensorStatusServiceTest extends Specification {

    def service = new SensorStatusService()

    @Unroll
    def "should return [#result] for [measurements=#measurements] and [alertOngoing=#alertOngoing]"() {

        expect:
        result == service.calculateStatus(measurements, alertOngoing)

        where:
        result             | measurements                                                                                                                                  | alertOngoing
        SensorStatus.OK    | []                                                                                                                                            | false
        SensorStatus.OK    | [SensorMeasurement.builder().co2(1000).build(), SensorMeasurement.builder().co2(1000).build(), SensorMeasurement.builder().co2(1000).build()] | false
        SensorStatus.OK    | [SensorMeasurement.builder().co2(1000).build(), SensorMeasurement.builder().co2(3000).build(), SensorMeasurement.builder().co2(3000).build()] | false
        SensorStatus.WARN  | [SensorMeasurement.builder().co2(3000).build(), SensorMeasurement.builder().co2(1000).build(), SensorMeasurement.builder().co2(1000).build()] | false
        SensorStatus.ALERT | [SensorMeasurement.builder().co2(3000).build(), SensorMeasurement.builder().co2(3000).build(), SensorMeasurement.builder().co2(3000).build()] | false
        SensorStatus.OK    | [SensorMeasurement.builder().co2(1000).build(), SensorMeasurement.builder().co2(1000).build(), SensorMeasurement.builder().co2(1000).build()] | true
    }
}

package org.rodzen.sensor.repository;

import org.rodzen.sensor.entity.SensorMeasurement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SensorMeasurementRepository extends CrudRepository<SensorMeasurement, Long> {

    Optional<SensorMeasurement> findByUuid(String uuid);

    @Query(value = "SELECT * FROM measurements WHERE uuid = :uuid ORDER BY time DESC LIMIT 3", nativeQuery = true)
    List<SensorMeasurement> findMostRecentByUuid(@Param("uuid") String uuid);

    @Query(value = "SELECT MAX(co2) FROM measurements WHERE uuid = :uuid AND time > CURRENT_TIMESTAMP() - 30", nativeQuery = true)
    Integer findMaxByUuid(@Param("uuid") String uuid);

    @Query(value = "SELECT AVG(co2) FROM measurements WHERE uuid = :uuid AND time > CURRENT_TIMESTAMP() - 30", nativeQuery = true)
    Integer findAvgByUuid(@Param("uuid") String uuid);
}

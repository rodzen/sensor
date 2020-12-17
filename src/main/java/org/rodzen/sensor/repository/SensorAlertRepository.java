package org.rodzen.sensor.repository;

import org.rodzen.sensor.entity.SensorAlert;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SensorAlertRepository extends CrudRepository<SensorAlert, Long> {

    List<SensorAlert> findByUuid(String uuid);

    @Query(value = "SELECT * FROM alerts WHERE uuid = :uuid ORDER BY start_time DESC LIMIT 1", nativeQuery = true)
    Optional<SensorAlert> findMostRecentByUuid(@Param("uuid") String uuid);
}

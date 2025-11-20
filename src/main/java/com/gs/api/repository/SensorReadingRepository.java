package com.gs.api.repository;

import com.gs.api.model.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

    List<SensorReading> findBySensorIdAndTimestampBetween(Long sensorId, Instant start, Instant end);

    List<SensorReading> findBySensor_Sector_Supervisor_Id(Long supervisorId);

    List<SensorReading> findBySensor_CodeAndSensor_Sector_Supervisor_Id(String sensorCode, Long supervisorId);

}

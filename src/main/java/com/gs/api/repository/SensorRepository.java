package com.gs.api.repository;

import com.gs.api.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, Long> {

    Optional<Sensor> findByCode(String code);

    Optional<Sensor> findBySectorId(Long sectorId);
}

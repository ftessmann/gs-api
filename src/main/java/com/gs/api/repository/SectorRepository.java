package com.gs.api.repository;

import com.gs.api.model.Sector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectorRepository extends JpaRepository<Sector, Long> {

    List<Sector> findBySupervisorId(Long supervisorId);
}

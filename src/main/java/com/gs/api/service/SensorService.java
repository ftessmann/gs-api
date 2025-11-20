package com.gs.api.service;

import com.gs.api.dto.request.SensorCreateRequestDTO;
import com.gs.api.dto.request.SensorUpdateRequestDTO;
import com.gs.api.dto.response.SensorResponseDTO;
import com.gs.api.model.Sensor;
import com.gs.api.model.Sector;
import com.gs.api.model.User;
import com.gs.api.repository.SensorRepository;
import com.gs.api.repository.SectorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;
    private final SectorRepository sectorRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public SensorResponseDTO createSensor(SensorCreateRequestDTO dto) {
        User authUser = authenticatedUserService.getAuthenticatedUser();

        Sector sector = sectorRepository.findById(dto.getSectorId())
                .filter(s -> s.getSupervisor().getId().equals(authUser.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado"));

        Sensor sensor = Sensor.builder()
                .code(dto.getCode())
                .sector(sector)
                .build();

        sensorRepository.save(sensor);

        return toResponseDTO(sensor);
    }

    public List<SensorResponseDTO> getMySensors() {
        User authUser = authenticatedUserService.getAuthenticatedUser();

        List<Sector> sectors = sectorRepository.findBySupervisorId(authUser.getId());
        return sectors.stream()
                .map(Sector::getSensor)
                .filter(s -> s != null)
                .map(this::toResponseDTO)
                .toList();
    }

    public SensorResponseDTO getMySensorById(Long id) {
        User authUser = authenticatedUserService.getAuthenticatedUser();

        Sensor sensor = sensorRepository.findById(id)
                .filter(s -> s.getSector().getSupervisor().getId().equals(authUser.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Sensor não encontrado"));

        return toResponseDTO(sensor);
    }

    public SensorResponseDTO updateMySensor(Long id, SensorUpdateRequestDTO dto) {
        User authUser = authenticatedUserService.getAuthenticatedUser();

        Sensor sensor = sensorRepository.findById(id)
                .filter(s -> s.getSector().getSupervisor().getId().equals(authUser.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Sensor não encontrado"));

        sensor.setCode(dto.getCode());
        sensorRepository.save(sensor);

        return toResponseDTO(sensor);
    }

    private SensorResponseDTO toResponseDTO(Sensor sensor) {
        return SensorResponseDTO.builder()
                .id(sensor.getId())
                .code(sensor.getCode())
                .sectorId(sensor.getSector().getId())
                .sectorName(sensor.getSector().getName())
                .build();
    }
}

package com.gs.api.service;

import com.gs.api.dto.request.SectorCreateRequestDTO;
import com.gs.api.dto.request.SectorUpdateRequestDTO;
import com.gs.api.dto.response.SectorResponseDTO;
import com.gs.api.model.Sector;
import com.gs.api.model.User;
import com.gs.api.repository.SectorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectorService {

    private final SectorRepository sectorRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public SectorResponseDTO createSector(SectorCreateRequestDTO dto) {
        User authUser = authenticatedUserService.getAuthenticatedUser();

        if (!authUser.getId().equals(dto.getSupervisorId())) {
            throw new RuntimeException("Você só pode criar setores para você mesmo");
        }

        Sector sector = Sector.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .supervisor(authUser)
                .build();

        sectorRepository.save(sector);

        return toResponseDTO(sector);
    }

    public List<SectorResponseDTO> getMySectors() {
        User authUser = authenticatedUserService.getAuthenticatedUser();
        List<Sector> sectors = sectorRepository.findBySupervisorId(authUser.getId());
        return sectors.stream().map(this::toResponseDTO).toList();
    }

    public SectorResponseDTO getMySectorById(Long id) {
        User authUser = authenticatedUserService.getAuthenticatedUser();

        Sector sector = sectorRepository.findById(id)
                .filter(s -> s.getSupervisor().getId().equals(authUser.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado"));

        return toResponseDTO(sector);
    }

    public SectorResponseDTO updateMySector(Long id, SectorUpdateRequestDTO dto) {
        User authUser = authenticatedUserService.getAuthenticatedUser();

        Sector sector = sectorRepository.findById(id)
                .filter(s -> s.getSupervisor().getId().equals(authUser.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado"));

        sector.setName(dto.getName());
        sector.setDescription(dto.getDescription());

        sectorRepository.save(sector);

        return toResponseDTO(sector);
    }

    public void deleteMySector(Long id) {
        User authUser = authenticatedUserService.getAuthenticatedUser();

        Sector sector = sectorRepository.findById(id)
                .filter(s -> s.getSupervisor().getId().equals(authUser.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado"));

        sectorRepository.delete(sector);
    }

    private SectorResponseDTO toResponseDTO(Sector sector) {
        return SectorResponseDTO.builder()
                .id(sector.getId())
                .name(sector.getName())
                .description(sector.getDescription())
                .supervisorId(sector.getSupervisor().getId())
                .supervisorName(sector.getSupervisor().getName())
                .build();
    }
}

package com.gs.api.dto.response;

import lombok.Builder;

@Builder
public record SensorResponseDTO(
        Long id,
        String code,
        Long sectorId,
        String sectorName
) {}

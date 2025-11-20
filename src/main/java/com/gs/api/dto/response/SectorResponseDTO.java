package com.gs.api.dto.response;

import lombok.Builder;

@Builder
public record SectorResponseDTO(
        Long id,
        String name,
        String description,
        Long supervisorId,
        String supervisorName
) {}

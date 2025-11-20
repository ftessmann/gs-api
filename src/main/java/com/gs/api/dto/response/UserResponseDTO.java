package com.gs.api.dto.response;

import lombok.Builder;

@Builder
public record UserResponseDTO(
        Long id,
        String name,
        String telephone,
        String username,
        String email
) {}

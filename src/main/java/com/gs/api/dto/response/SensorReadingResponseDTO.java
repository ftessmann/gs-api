package com.gs.api.dto.response;

import lombok.Builder;

import java.time.Instant;

@Builder
public record SensorReadingResponseDTO(
        Long id,
        Long sensorId,
        String sensorCode,
        Double temperature,
        Double humidity,
        Double co,
        Double co2,
        Double ch4,
        Double h2s,
        Instant timestamp
) {}

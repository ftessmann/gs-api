package com.gs.api.dto.response;

import com.gs.api.enums.NotificationStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record NotificationResponseDTO(
        Long id,
        String title,
        String description,
        String metric,
        Double measuredValue,
        Double thresholdValue,
        NotificationStatus status,
        Instant createdAt,
        Instant readAt,
        Long sectorId,
        String sectorName
) {}

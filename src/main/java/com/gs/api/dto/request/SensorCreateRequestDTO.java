package com.gs.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SensorCreateRequestDTO {

    @NotBlank
    private String code;

    @NotNull
    private Long sectorId;
}

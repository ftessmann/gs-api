package com.gs.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SectorCreateRequestDTO {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Long supervisorId;
}

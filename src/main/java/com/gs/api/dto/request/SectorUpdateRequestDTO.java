package com.gs.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SectorUpdateRequestDTO {

    @NotBlank
    private String name;

    private String description;
}

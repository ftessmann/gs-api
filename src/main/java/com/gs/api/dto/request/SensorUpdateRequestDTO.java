package com.gs.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SensorUpdateRequestDTO {

    @NotBlank
    private String code;
}

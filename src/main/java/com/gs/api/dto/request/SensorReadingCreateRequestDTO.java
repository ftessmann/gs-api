package com.gs.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SensorReadingCreateRequestDTO {

    @NotNull
    private String sensorCode;

    private Double temperature;
    private Double humidity;
    private Double co;
    private Double co2;
    private Double ch4;
    private Double h2s;
}

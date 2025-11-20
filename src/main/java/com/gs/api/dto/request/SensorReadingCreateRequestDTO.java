package com.gs.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SensorReadingCreateRequestDTO {

    @NotNull
    @JsonProperty("code")
    private String sensorCode;

    @JsonProperty("temperatura")
    private Double temperature;

    @JsonProperty("umidade")
    private Double humidity;

    @JsonProperty("co")
    private Double co;

    @JsonProperty("co2")
    private Double co2;

    @JsonProperty("ch4")
    private Double ch4;

    @JsonProperty("h2s")
    private Double h2s;
}

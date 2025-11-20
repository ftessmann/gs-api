package com.gs.api.controller;

import com.gs.api.dto.request.SensorReadingCreateRequestDTO;
import com.gs.api.dto.response.SensorReadingResponseDTO;
import com.gs.api.service.AuthenticatedUserService;
import com.gs.api.service.SensorReadingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/sensor-readings")
@RequiredArgsConstructor
@Tag(name = "Sensor Readings", description = "Endpoints para recebimento e consulta de leituras dos sensores")
public class SensorReadingController {

    private final SensorReadingService sensorReadingService;
    private final AuthenticatedUserService authenticatedUserService;

    @Value("${sensor.api-key}")
    private String expectedApiKey;

    @PostMapping
    @SecurityRequirements
    @Operation(
            summary = "Registrar leitura do sensor (webhook)",
            description = "Webhook público que recebe os dados do sensor (temperatura, umidade, gases, etc.) " +
                    "e registra a leitura, gerando notificações caso algum valor ultrapasse o limite. " +
                    "Protegido por API key via header X-API-KEY."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Leitura registrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SensorReadingResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "API key inválida ou ausente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sensor não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content
            )
    })
    public ResponseEntity<SensorReadingResponseDTO> createReading(
            @RequestHeader(name = "X-API-KEY", required = true) String apiKey,
            @Valid @RequestBody SensorReadingCreateRequestDTO dto
    ) {
        if (!expectedApiKey.equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        SensorReadingResponseDTO reading = sensorReadingService.createReading(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reading);
    }

    @GetMapping
    @Operation(
            summary = "Listar leituras dos meus sensores",
            description = "Retorna as leituras associadas aos sensores dos setores pertencentes ao usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Leituras encontradas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SensorReadingResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content
            )
    })
    public ResponseEntity<List<SensorReadingResponseDTO>> getMyReadings() {
        Long userId = authenticatedUserService.getAuthenticatedUserId();
        List<SensorReadingResponseDTO> readings = sensorReadingService.getReadingsForUser(userId);
        return ResponseEntity.ok(readings);
    }

    @GetMapping("/sensor/{sensorCode}")
    @Operation(
            summary = "Listar leituras por sensor",
            description = "Retorna as leituras de um sensor específico, desde que pertença a um setor do usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Leituras encontradas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SensorReadingResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sensor não encontrado ou não pertence ao usuário",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content
            )
    })
    public ResponseEntity<List<SensorReadingResponseDTO>> getReadingsBySensor(
            @PathVariable String sensorCode
    ) {
        Long userId = authenticatedUserService.getAuthenticatedUserId();

        List<SensorReadingResponseDTO> readings =
                sensorReadingService.getReadingsForUserSensor(userId, sensorCode);
        return ResponseEntity.ok(readings);
    }

    @GetMapping("/sensor/{sensorId}/range")
    @Operation(
            summary = "Listar leituras por sensor e intervalo de tempo",
            description = "Retorna as leituras de um sensor específico, pertencente a um setor do usuário autenticado, " +
                    "entre as datas/hora informadas (start e end em formato ISO-8601)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Leituras encontradas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SensorReadingResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sensor não encontrado ou não pertence ao usuário",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parâmetros de data/hora inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content
            )
    })
    public ResponseEntity<List<SensorReadingResponseDTO>> getReadingsBySensorAndRange(
            @PathVariable Long sensorId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end
    ) {
        Long userId = authenticatedUserService.getAuthenticatedUserId();

        List<SensorReadingResponseDTO> readings =
                sensorReadingService.getReadingsForUserSensorBetween(userId, sensorId, start, end);

        return ResponseEntity.ok(readings);
    }
}

package com.gs.api.controller;

import com.gs.api.dto.request.SensorCreateRequestDTO;
import com.gs.api.dto.request.SensorUpdateRequestDTO;
import com.gs.api.dto.response.SensorResponseDTO;
import com.gs.api.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
@Tag(name = "Sensors", description = "Endpoints para gerenciamento de sensores por setor")
public class SensorController {

    private final SensorService sensorService;

    @PostMapping
    @Operation(summary = "Criar novo sensor",
            description = "Cria um sensor associado a um setor do usuário autenticado.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Sensor criado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SensorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Setor não encontrado ou não pertence ao usuário",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content
            )
    })
    public ResponseEntity<SensorResponseDTO> createSensor(@RequestBody SensorCreateRequestDTO dto) {
        SensorResponseDTO sensor = sensorService.createSensor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sensor);
    }

    @GetMapping
    @Operation(summary = "Listar meus sensores",
            description = "Lista todos os sensores dos setores pertencentes ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sensores encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SensorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content
            )
    })
    public ResponseEntity<List<SensorResponseDTO>> getMySensors() {
        List<SensorResponseDTO> sensors = sensorService.getMySensors();
        return ResponseEntity.ok(sensors);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sensor por id",
            description = "Retorna um sensor associado a um setor do usuário autenticado.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sensor encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SensorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sensor não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content
            )
    })
    public ResponseEntity<SensorResponseDTO> getMySensorById(@PathVariable Long id) {
        SensorResponseDTO sensor = sensorService.getMySensorById(id);
        return ResponseEntity.ok(sensor);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar sensor",
            description = "Atualiza os dados de um sensor dos setores do usuário autenticado.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sensor atualizado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SensorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sensor não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content
            )
    })
    public ResponseEntity<SensorResponseDTO> updateMySensor(
            @PathVariable Long id,
            @RequestBody SensorUpdateRequestDTO dto
    ) {
        SensorResponseDTO sensor = sensorService.updateMySensor(id, dto);
        return ResponseEntity.ok(sensor);
    }
}

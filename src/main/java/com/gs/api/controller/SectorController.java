package com.gs.api.controller;

import com.gs.api.dto.request.SectorCreateRequestDTO;
import com.gs.api.dto.request.SectorUpdateRequestDTO;
import com.gs.api.dto.response.SectorResponseDTO;
import com.gs.api.service.SectorService;
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
@RequestMapping("/api/sectors")
@RequiredArgsConstructor
@Tag(name = "Sectors", description = "Endpoints para gerenciamento de setores do usuário autenticado")
public class SectorController {

    private final SectorService sectorService;

    @PostMapping
    @Operation(summary = "Criar novo setor",
            description = "Cria um setor vinculado ao usuário autenticado (supervisor).")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Setor criado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SectorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno",
                    content = @Content
            )
    })
    public ResponseEntity<SectorResponseDTO> createSector(@RequestBody SectorCreateRequestDTO dto) {
        SectorResponseDTO sector = sectorService.createSector(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sector);
    }

    @GetMapping
    @Operation(summary = "Listar meus setores",
            description = "Retorna todos os setores vinculados ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Setores encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SectorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content
            )
    })
    public ResponseEntity<List<SectorResponseDTO>> getMySectors() {
        List<SectorResponseDTO> sectors = sectorService.getMySectors();
        return ResponseEntity.ok(sectors);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar setor por id",
            description = "Retorna um setor do usuário autenticado pelo seu identificador.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Setor encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SectorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Setor não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content
            )
    })
    public ResponseEntity<SectorResponseDTO> getMySectorById(@PathVariable Long id) {
        SectorResponseDTO sector = sectorService.getMySectorById(id);
        return ResponseEntity.ok(sector);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar setor",
            description = "Atualiza os dados de um setor do usuário autenticado.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Setor atualizado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SectorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Setor não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content
            )
    })
    public ResponseEntity<SectorResponseDTO> updateMySector(
            @PathVariable Long id,
            @RequestBody SectorUpdateRequestDTO dto
    ) {
        SectorResponseDTO sector = sectorService.updateMySector(id, dto);
        return ResponseEntity.ok(sector);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar setor",
            description = "Remove um setor pertencente ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Setor deletado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Setor não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deleteMySector(@PathVariable Long id) {
        sectorService.deleteMySector(id);
        return ResponseEntity.noContent().build();
    }
}

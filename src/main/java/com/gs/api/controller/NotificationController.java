package com.gs.api.controller;

import com.gs.api.dto.response.NotificationResponseDTO;
import com.gs.api.enums.NotificationStatus;
import com.gs.api.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Endpoints para gerenciamento de notificações do usuário autenticado")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Listar minhas notificações",
            description = "Retorna todas as notificações do usuário autenticado, podendo filtrar por status.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Notificações encontradas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content
            )
    })
    public ResponseEntity<List<NotificationResponseDTO>> getMyNotifications(
            @RequestParam(required = false) NotificationStatus status
    ) {
        List<NotificationResponseDTO> notifications = notificationService.getMyNotifications(status);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Marcar notificação como lida",
            description = "Marca uma notificação do usuário autenticado como lida.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Notificação marcada como lida",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Notificação não encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content
            )
    })
    public ResponseEntity<NotificationResponseDTO> markAsRead(@PathVariable Long id) {
        NotificationResponseDTO notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(notification);
    }
}

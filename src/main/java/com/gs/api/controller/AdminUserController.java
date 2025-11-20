package com.gs.api.controller;

import com.gs.api.dto.request.ChangePasswordRequestDTO;
import com.gs.api.dto.response.UserResponseDTO;
import com.gs.api.model.User;
import com.gs.api.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(name = "Admin - Users", description = "Endpoints administrativos para gestão de usuários")
public class AdminUserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos os usuários",
            description = "Retorna todos os usuários cadastrados. Requer perfil ADMIN.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuários encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado (não é ADMIN)",
                    content = @Content
            )
    })
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar usuário por id",
            description = "Retorna os dados de um usuário pelo id. Requer perfil ADMIN.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado (não é ADMIN)",
                    content = @Content
            )
    })
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return ResponseEntity.ok(toResponseDTO(user));
    }

    @PatchMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Alterar senha de um usuário",
            description = "Permite ao ADMIN alterar a senha de qualquer usuário.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Senha alterada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado (não é ADMIN)",
                    content = @Content
            )
    })
    public ResponseEntity<UserResponseDTO> changeUserPassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequestDTO dto
    ) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(toResponseDTO(user));
    }

    private UserResponseDTO toResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .telephone(user.getTelephone())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}

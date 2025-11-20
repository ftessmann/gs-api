package com.gs.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Me", description = "Endpoint para obter informações do usuário autenticado")
public class MeController {

    @GetMapping("/me")
    @Operation(
            summary = "Obter dados do usuário logado",
            description = "Retorna username e authorities do usuário autenticado com base no token JWT."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Dados do usuário retornados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = MeResponseSchema.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content
            )
    })
    public Map<String, Object> me(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return Map.of(
                "username", userDetails.getUsername(),
                "authorities", userDetails.getAuthorities()
        );
    }

    static class MeResponseSchema {
        public String username;
        public Object authorities;
    }
}

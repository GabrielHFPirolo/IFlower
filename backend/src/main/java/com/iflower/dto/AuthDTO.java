package com.iflower.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public final class AuthDTO {

    private AuthDTO() {
    }

    public record LoginRequest(
            @NotBlank(message = "Email é obrigatório") @Email(message = "Email inválido") String email,
            @NotBlank(message = "Senha é obrigatória") String senha) {
    }

    public record LoginResponse(String token, String tipo, long expiraEmSegundos, UsuarioDTO.Response usuario) {
    }
}

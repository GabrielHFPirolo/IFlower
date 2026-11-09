package com.iflower.security;

import com.iflower.model.enums.Papel;

/** Identidade extraída do token JWT; é o "principal" disponível nos controllers. */
public record UsuarioAutenticado(Long id, String email, Papel papel) {
}

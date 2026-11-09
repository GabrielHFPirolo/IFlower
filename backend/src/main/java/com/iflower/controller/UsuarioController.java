package com.iflower.controller;

import com.iflower.dto.UsuarioDTO;
import com.iflower.security.UsuarioAutenticado;
import com.iflower.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios/me")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public UsuarioDTO.Response perfil(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return usuarioService.buscarPerfil(usuario.id());
    }

    @PutMapping
    public UsuarioDTO.Response atualizar(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                         @Valid @RequestBody UsuarioDTO.Atualizacao req) {
        return usuarioService.atualizarPerfil(usuario.id(), req);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        usuarioService.desativar(usuario.id());
    }
}

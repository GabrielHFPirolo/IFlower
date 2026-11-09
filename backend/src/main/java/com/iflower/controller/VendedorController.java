package com.iflower.controller;

import com.iflower.dto.UsuarioDTO;
import com.iflower.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vendedores")
public class VendedorController {

    private final UsuarioService usuarioService;

    public VendedorController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDTO.Response cadastrar(@Valid @RequestBody UsuarioDTO.VendedorCadastro req) {
        return usuarioService.cadastrarVendedor(req);
    }
}

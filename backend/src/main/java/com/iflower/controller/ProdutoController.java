package com.iflower.controller;

import com.iflower.dto.ProdutoDTO;
import com.iflower.security.UsuarioAutenticado;
import com.iflower.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoDTO.Response criar(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                     @Valid @RequestBody ProdutoDTO.Request req) {
        return produtoService.criar(usuario.id(), req);
    }

    @GetMapping("/me")
    public List<ProdutoDTO.Response> meus(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return produtoService.listarDoVendedor(usuario.id());
    }

    @GetMapping("/{id}")
    public ProdutoDTO.Response buscar(@PathVariable Long id) {
        return produtoService.buscar(id);
    }

    @PutMapping("/{id}")
    public ProdutoDTO.Response atualizar(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                         @PathVariable Long id, @Valid @RequestBody ProdutoDTO.Request req) {
        return produtoService.atualizar(usuario.id(), id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable Long id) {
        produtoService.excluir(usuario.id(), id);
    }
}

package com.iflower.controller;

import com.iflower.dto.AvaliacaoDTO;
import com.iflower.security.UsuarioAutenticado;
import com.iflower.service.AvaliacaoService;
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
@RequestMapping("/api")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @PostMapping("/lojas/{lojaId}/avaliacoes")
    @ResponseStatus(HttpStatus.CREATED)
    public AvaliacaoDTO.Response criar(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                       @PathVariable Long lojaId,
                                       @Valid @RequestBody AvaliacaoDTO.Request req) {
        return avaliacaoService.criar(usuario.id(), lojaId, req);
    }

    @GetMapping("/lojas/{lojaId}/avaliacoes")
    public List<AvaliacaoDTO.Response> listar(@PathVariable Long lojaId) {
        return avaliacaoService.listarPorLoja(lojaId);
    }

    @PutMapping("/avaliacoes/{id}")
    public AvaliacaoDTO.Response editar(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                        @PathVariable Long id, @Valid @RequestBody AvaliacaoDTO.Request req) {
        return avaliacaoService.editar(usuario.id(), id, req);
    }

    @DeleteMapping("/avaliacoes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable Long id) {
        avaliacaoService.excluir(usuario.id(), id);
    }
}

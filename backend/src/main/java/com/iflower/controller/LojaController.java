package com.iflower.controller;

import com.iflower.dto.LojaDTO;
import com.iflower.dto.ProdutoDTO;
import com.iflower.security.UsuarioAutenticado;
import com.iflower.service.LojaService;
import com.iflower.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lojas")
public class LojaController {

    private final LojaService lojaService;
    private final ProdutoService produtoService;

    public LojaController(LojaService lojaService, ProdutoService produtoService) {
        this.lojaService = lojaService;
        this.produtoService = produtoService;
    }

    /** RF007 - busca por nome (parcial); filtros opcionais de cidade e UF. */
    @GetMapping
    public List<LojaDTO.Response> listar(@RequestParam(required = false) String nome,
                                         @RequestParam(required = false) String cidade,
                                         @RequestParam(required = false) String uf) {
        return lojaService.listar(nome, cidade, uf);
    }

    /** RF004 - lojas da cidade do cliente logado. */
    @GetMapping("/proximas")
    public List<LojaDTO.Response> proximas(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return lojaService.proximas(usuario.id());
    }

    @GetMapping("/me")
    public LojaDTO.Response minhaLoja(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return lojaService.minhaLoja(usuario.id());
    }

    @PutMapping("/me")
    public LojaDTO.Response atualizarMinhaLoja(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                               @Valid @RequestBody LojaDTO.Atualizacao req) {
        return lojaService.atualizarPerfil(usuario.id(), req);
    }

    @GetMapping("/{id}")
    public LojaDTO.Response buscar(@PathVariable Long id) {
        return lojaService.buscar(id);
    }

    @GetMapping("/{id}/produtos")
    public List<ProdutoDTO.Response> produtos(@PathVariable Long id) {
        return produtoService.listarPorLoja(id);
    }
}

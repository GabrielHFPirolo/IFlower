package com.iflower.controller;

import com.iflower.dto.CarrinhoDTO;
import com.iflower.security.UsuarioAutenticado;
import com.iflower.service.CarrinhoService;
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

@RestController
@RequestMapping("/api/carrinho")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    @GetMapping
    public CarrinhoDTO.Response obter(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return carrinhoService.obter(usuario.id());
    }

    @PostMapping("/itens")
    @ResponseStatus(HttpStatus.CREATED)
    public CarrinhoDTO.Response adicionar(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                          @Valid @RequestBody CarrinhoDTO.AdicionarItem req) {
        return carrinhoService.adicionarItem(usuario.id(), req);
    }

    @PutMapping("/itens/{itemId}")
    public CarrinhoDTO.Response alterarQuantidade(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                  @PathVariable Long itemId,
                                                  @Valid @RequestBody CarrinhoDTO.AlterarQuantidade req) {
        return carrinhoService.alterarQuantidade(usuario.id(), itemId, req);
    }

    @DeleteMapping("/itens/{itemId}")
    public CarrinhoDTO.Response remover(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                        @PathVariable Long itemId) {
        return carrinhoService.removerItem(usuario.id(), itemId);
    }

    @DeleteMapping
    public CarrinhoDTO.Response limpar(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return carrinhoService.limpar(usuario.id());
    }
}

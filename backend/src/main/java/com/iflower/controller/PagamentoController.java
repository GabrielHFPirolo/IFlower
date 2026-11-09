package com.iflower.controller;

import com.iflower.dto.PagamentoDTO;
import com.iflower.security.UsuarioAutenticado;
import com.iflower.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping("/pedidos/{pedidoId}/pagamento")
    @ResponseStatus(HttpStatus.CREATED)
    public PagamentoDTO.Response pagar(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                       @PathVariable Long pedidoId,
                                       @Valid @RequestBody PagamentoDTO.Request req) {
        return pagamentoService.pagar(usuario.id(), pedidoId, req);
    }

    /** Simula a compensação do boleto (feita pela loja). */
    @PostMapping("/pagamentos/{id}/confirmar")
    public PagamentoDTO.Response confirmar(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                           @PathVariable Long id) {
        return pagamentoService.confirmarBoleto(usuario.id(), id);
    }
}

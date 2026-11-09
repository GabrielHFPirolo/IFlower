package com.iflower.controller;

import com.iflower.dto.PedidoDTO;
import com.iflower.model.enums.StatusPedido;
import com.iflower.security.UsuarioAutenticado;
import com.iflower.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    /** Finaliza a compra: transforma o carrinho em pedido. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoDTO.Response criar(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                    @Valid @RequestBody PedidoDTO.CriarRequest req) {
        return pedidoService.criar(usuario.id(), req);
    }

    /** Cliente: histórico. Vendedor: pedidos da loja. */
    @GetMapping
    public List<PedidoDTO.Response> listar(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                           @RequestParam(required = false) StatusPedido status) {
        return pedidoService.listar(usuario, status);
    }

    @GetMapping("/{id}")
    public PedidoDTO.Response buscar(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable Long id) {
        return pedidoService.buscar(usuario, id);
    }

    @PatchMapping("/{id}/status")
    public PedidoDTO.Response atualizarStatus(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                              @PathVariable Long id,
                                              @Valid @RequestBody PedidoDTO.StatusRequest req) {
        return pedidoService.atualizarStatus(usuario.id(), id, req.status());
    }

    @PostMapping("/{id}/cancelar")
    public PedidoDTO.Response cancelar(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable Long id) {
        return pedidoService.cancelar(usuario.id(), id);
    }
}

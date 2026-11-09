package com.iflower.dto;

import com.iflower.model.Entrega;
import com.iflower.model.ItemPedido;
import com.iflower.model.Pedido;
import com.iflower.model.enums.StatusPedido;
import com.iflower.model.enums.TipoRetirada;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public final class PedidoDTO {

    private PedidoDTO() {
    }

    /** RF011 - data, horário e retirada na loja ou entrega. */
    public record EntregaRequest(
            @NotNull(message = "Informe se é entrega ou retirada na loja") TipoRetirada tipoRetirada,
            @NotNull(message = "Data é obrigatória")
            @FutureOrPresent(message = "A data não pode estar no passado") LocalDate dataEntrega,
            @NotNull(message = "Horário é obrigatório") LocalTime horario,
            @Valid EnderecoDTO endereco) {
    }

    /** RF009 / RF017 - finalizar compra: o pedido nasce do carrinho do cliente. */
    public record CriarRequest(@NotNull(message = "Dados de entrega são obrigatórios") @Valid EntregaRequest entrega) {
    }

    public record StatusRequest(@NotNull(message = "Status é obrigatório") StatusPedido status) {
    }

    public record ItemResponse(Long produtoId, String nome, int quantidade, BigDecimal precoUnitario,
                               String personalizacao, BigDecimal subtotal) {

        public static ItemResponse from(ItemPedido i) {
            return new ItemResponse(i.getProduto().getId(), i.getProduto().getNome(), i.getQuantidade(),
                    i.getPrecoUnitario(), i.getPersonalizacao(), i.subtotal());
        }
    }

    public record EntregaResponse(Long id, TipoRetirada tipoRetirada, LocalDate dataEntrega, LocalTime horario,
                                  EnderecoDTO endereco, boolean selecionada, boolean cancelada) {

        public static EntregaResponse from(Entrega e) {
            if (e == null) {
                return null;
            }
            return new EntregaResponse(e.getId(), e.getTipoRetirada(), e.getDataEntrega(), e.getHorario(),
                    EnderecoDTO.from(e.getEnderecoEntrega()), e.isSelecionada(), e.isCancelada());
        }
    }

    /** RF012 - todas as informações do pedido (itens, entrega, pagamentos e total). */
    public record Response(Long id, Long clienteId, String clienteNome, Long lojaId, String lojaNome,
                           LocalDateTime dataPedido, StatusPedido status, BigDecimal valorTotal,
                           List<ItemResponse> itens, EntregaResponse entrega,
                           List<PagamentoDTO.Response> pagamentos) {

        public static Response from(Pedido p) {
            return new Response(p.getId(), p.getCliente().getId(), p.getCliente().getNome(),
                    p.getLoja().getId(), p.getLoja().getNome(), p.getDataPedido(), p.getStatus(),
                    p.getValorTotal(),
                    p.getItens().stream().map(ItemResponse::from).toList(),
                    EntregaResponse.from(p.getEntrega()),
                    p.getPagamentos().stream().map(PagamentoDTO.Response::from).toList());
        }
    }
}

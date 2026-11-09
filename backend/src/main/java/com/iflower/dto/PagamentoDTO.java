package com.iflower.dto;

import com.iflower.model.enums.StatusPagamento;
import com.iflower.model.enums.TipoPagamento;
import com.iflower.model.pagamento.PagamentoBase;
import com.iflower.model.pagamento.PagamentoBoleto;
import com.iflower.model.pagamento.PagamentoCartao;
import com.iflower.model.pagamento.PagamentoPix;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class PagamentoDTO {

    private PagamentoDTO() {
    }

    /** Os campos exigidos variam por tipo; quem valida é o PagamentoCriador do tipo escolhido. */
    public record Request(
            @NotNull(message = "Tipo de pagamento é obrigatório") TipoPagamento tipo,
            String chavePix,
            @Pattern(regexp = "\\d{4}", message = "Informe os 4 últimos dígitos do cartão") String ultimosDigitos) {
    }

    public record Response(Long id, Long pedidoId, TipoPagamento tipo, StatusPagamento status, BigDecimal valor,
                           LocalDateTime dataPagamento, String chavePix, String ultimosDigitos,
                           String codigoBarras) {

        public static Response from(PagamentoBase p) {
            String chave = p instanceof PagamentoPix px ? px.getChavePix() : null;
            String digitos = p instanceof PagamentoCartao pc ? pc.getUltimosDigitos() : null;
            String codigo = p instanceof PagamentoBoleto pb ? pb.getCodigoBarras() : null;
            return new Response(p.getId(), p.getPedido().getId(), p.getTipo(), p.getStatus(), p.getValor(),
                    p.getDataPagamento(), chave, digitos, codigo);
        }
    }
}

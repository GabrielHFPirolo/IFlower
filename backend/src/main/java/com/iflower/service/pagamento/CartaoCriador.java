package com.iflower.service.pagamento;

import com.iflower.dto.PagamentoDTO;
import com.iflower.exception.RegraNegocioException;
import com.iflower.model.Pedido;
import com.iflower.model.enums.TipoPagamento;
import com.iflower.model.pagamento.PagamentoBase;
import com.iflower.model.pagamento.PagamentoCartao;
import org.springframework.stereotype.Component;

@Component
public class CartaoCriador implements PagamentoCriador {

    @Override
    public TipoPagamento tipo() {
        return TipoPagamento.CARTAO;
    }

    @Override
    public PagamentoBase criar(Pedido pedido, PagamentoDTO.Request dados) {
        if (dados.ultimosDigitos() == null) {
            throw new RegraNegocioException("Informe os 4 últimos dígitos do cartão.");
        }
        return new PagamentoCartao(pedido, dados.ultimosDigitos());
    }
}

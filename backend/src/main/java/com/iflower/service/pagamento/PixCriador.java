package com.iflower.service.pagamento;

import com.iflower.dto.PagamentoDTO;
import com.iflower.exception.RegraNegocioException;
import com.iflower.model.Pedido;
import com.iflower.model.enums.TipoPagamento;
import com.iflower.model.pagamento.PagamentoBase;
import com.iflower.model.pagamento.PagamentoPix;
import org.springframework.stereotype.Component;

@Component
public class PixCriador implements PagamentoCriador {

    @Override
    public TipoPagamento tipo() {
        return TipoPagamento.PIX;
    }

    @Override
    public PagamentoBase criar(Pedido pedido, PagamentoDTO.Request dados) {
        if (dados.chavePix() == null || dados.chavePix().isBlank()) {
            throw new RegraNegocioException("Informe a chave Pix.");
        }
        return new PagamentoPix(pedido, dados.chavePix().trim());
    }
}

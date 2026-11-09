package com.iflower.service.pagamento;

import com.iflower.dto.PagamentoDTO;
import com.iflower.model.Pedido;
import com.iflower.model.enums.TipoPagamento;
import com.iflower.model.pagamento.PagamentoBase;
import com.iflower.model.pagamento.PagamentoBoleto;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class BoletoCriador implements PagamentoCriador {

    private final SecureRandom random = new SecureRandom();

    @Override
    public TipoPagamento tipo() {
        return TipoPagamento.BOLETO;
    }

    @Override
    public PagamentoBase criar(Pedido pedido, PagamentoDTO.Request dados) {
        return new PagamentoBoleto(pedido, gerarCodigoBarras());
    }

    /** Simulação: código numérico de 44 posições (não é um boleto bancário real). */
    private String gerarCodigoBarras() {
        StringBuilder sb = new StringBuilder(44);
        for (int i = 0; i < 44; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}

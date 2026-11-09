package com.iflower.service;

import com.iflower.dto.PagamentoDTO;
import com.iflower.exception.ConflitoException;
import com.iflower.exception.ProibidoException;
import com.iflower.exception.RecursoNaoEncontradoException;
import com.iflower.exception.RegraNegocioException;
import com.iflower.model.Pedido;
import com.iflower.model.enums.StatusPagamento;
import com.iflower.model.enums.StatusPedido;
import com.iflower.model.enums.TipoPagamento;
import com.iflower.model.pagamento.PagamentoBase;
import com.iflower.model.pagamento.PagamentoBoleto;
import com.iflower.repository.PagamentoRepository;
import com.iflower.service.pagamento.PagamentoCriador;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * RF019 - pagamento do pedido.
 * Pagamento e atualização do status do pedido acontecem na mesma transação (RNF007).
 * Os gateways são simulados: Pix e cartão aprovam na hora; boleto fica pendente até a confirmação.
 */
@Service
@Transactional
public class PagamentoService {

    private final PedidoService pedidoService;
    private final PagamentoRepository pagamentoRepository;
    private final Map<TipoPagamento, PagamentoCriador> criadores = new EnumMap<>(TipoPagamento.class);

    public PagamentoService(PedidoService pedidoService, PagamentoRepository pagamentoRepository,
                            List<PagamentoCriador> criadores) {
        this.pedidoService = pedidoService;
        this.pagamentoRepository = pagamentoRepository;
        criadores.forEach(c -> this.criadores.put(c.tipo(), c));
    }

    public PagamentoDTO.Response pagar(Long clienteId, Long pedidoId, PagamentoDTO.Request req) {
        Pedido pedido = pedidoService.buscarEntidade(pedidoId);
        if (!pedido.getCliente().getId().equals(clienteId)) {
            throw new ProibidoException("Este pedido não pertence a você.");
        }
        if (pedido.getStatus() != StatusPedido.AGUARDANDO_PAGAMENTO) {
            throw new RegraNegocioException("O pedido não está aguardando pagamento (status atual: "
                    + pedido.getStatus() + ").");
        }
        boolean jaPagoOuPendente = pedido.getPagamentos().stream()
                .anyMatch(p -> p.getStatus() != StatusPagamento.RECUSADO);
        if (jaPagoOuPendente) {
            throw new ConflitoException("Já existe um pagamento aprovado ou pendente para este pedido.");
        }

        PagamentoCriador criador = criadores.get(req.tipo());
        if (criador == null) {
            throw new RegraNegocioException("Forma de pagamento não suportada: " + req.tipo() + ".");
        }

        PagamentoBase pagamento = criador.criar(pedido, req);
        pagamento.processarPagamento(pedido.getValorTotal());
        pagamentoRepository.save(pagamento);
        pedido.registrarPagamento(pagamento);

        if (pagamento.getStatus() == StatusPagamento.APROVADO) {
            pedido.confirmarPagamento();
        }
        return PagamentoDTO.Response.from(pagamento);
    }

    /** Simula a compensação bancária de um boleto, confirmada pela loja. */
    public PagamentoDTO.Response confirmarBoleto(Long vendedorId, Long pagamentoId) {
        PagamentoBase pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pagamento não encontrado."));
        Pedido pedido = pagamento.getPedido();
        if (!pedido.getLoja().getVendedor().getId().equals(vendedorId)) {
            throw new ProibidoException("Este pagamento pertence a outra loja.");
        }
        if (!(pagamento instanceof PagamentoBoleto boleto)) {
            throw new RegraNegocioException("Somente boletos exigem confirmação manual.");
        }
        boleto.confirmarCompensacao();
        if (pedido.getStatus() == StatusPedido.AGUARDANDO_PAGAMENTO) {
            pedido.confirmarPagamento();
        }
        return PagamentoDTO.Response.from(boleto);
    }
}

package com.iflower.service;

import com.iflower.dto.AvaliacaoDTO;
import com.iflower.exception.ConflitoException;
import com.iflower.exception.ProibidoException;
import com.iflower.exception.RecursoNaoEncontradoException;
import com.iflower.exception.RegraNegocioException;
import com.iflower.model.Avaliacao;
import com.iflower.model.Cliente;
import com.iflower.model.Loja;
import com.iflower.model.enums.StatusPedido;
import com.iflower.repository.AvaliacaoRepository;
import com.iflower.repository.ClienteRepository;
import com.iflower.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** RF003 e RF022 - avaliação de lojas: só quem já recebeu um pedido, uma avaliação por loja. */
@Service
@Transactional
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;
    private final LojaService lojaService;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository, ClienteRepository clienteRepository,
                            PedidoRepository pedidoRepository, LojaService lojaService) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;
        this.lojaService = lojaService;
    }

    public AvaliacaoDTO.Response criar(Long clienteId, Long lojaId, AvaliacaoDTO.Request req) {
        Loja loja = lojaService.buscarEntidade(lojaId);
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado."));
        if (!pedidoRepository.existsByClienteIdAndLojaIdAndStatus(clienteId, lojaId, StatusPedido.ENTREGUE)) {
            throw new RegraNegocioException("Você só pode avaliar lojas das quais já recebeu um pedido.");
        }
        if (avaliacaoRepository.findByClienteIdAndLojaId(clienteId, lojaId).isPresent()) {
            throw new ConflitoException("Você já avaliou esta loja. Edite a avaliação existente.");
        }
        Avaliacao avaliacao = avaliacaoRepository.save(new Avaliacao(cliente, loja, req.nota(), req.comentario()));
        return AvaliacaoDTO.Response.from(avaliacao);
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoDTO.Response> listarPorLoja(Long lojaId) {
        lojaService.buscarEntidade(lojaId);
        return avaliacaoRepository.findByLojaIdOrderByDataAvaliacaoDesc(lojaId)
                .stream().map(AvaliacaoDTO.Response::from).toList();
    }

    public AvaliacaoDTO.Response editar(Long clienteId, Long avaliacaoId, AvaliacaoDTO.Request req) {
        Avaliacao avaliacao = buscarDoCliente(clienteId, avaliacaoId);
        avaliacao.editar(req.nota(), req.comentario());
        return AvaliacaoDTO.Response.from(avaliacao);
    }

    public void excluir(Long clienteId, Long avaliacaoId) {
        avaliacaoRepository.delete(buscarDoCliente(clienteId, avaliacaoId));
    }

    private Avaliacao buscarDoCliente(Long clienteId, Long avaliacaoId) {
        Avaliacao avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Avaliação não encontrada."));
        if (!avaliacao.getCliente().getId().equals(clienteId)) {
            throw new ProibidoException("Esta avaliação pertence a outro cliente.");
        }
        return avaliacao;
    }
}

package com.iflower.service;

import com.iflower.dto.PedidoDTO;
import com.iflower.exception.ProibidoException;
import com.iflower.exception.RecursoNaoEncontradoException;
import com.iflower.exception.RegraNegocioException;
import com.iflower.model.Carrinho;
import com.iflower.model.Cliente;
import com.iflower.model.Endereco;
import com.iflower.model.Entrega;
import com.iflower.model.ItemCarrinho;
import com.iflower.model.ItemPedido;
import com.iflower.model.Loja;
import com.iflower.model.Pedido;
import com.iflower.model.enums.StatusPedido;
import com.iflower.model.enums.TipoRetirada;
import com.iflower.repository.CarrinhoRepository;
import com.iflower.repository.PedidoRepository;
import com.iflower.security.UsuarioAutenticado;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * RF009/RF017 (finalizar compra), RF010 (acompanhar), RF011 (entrega/retirada),
 * RF012 (informações do pedido), RF013/RF014 (consulta/histórico), RF015 (status).
 * Todo o fluxo de criação é uma única transação (RNF007).
 */
@Service
@Transactional
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final LojaService lojaService;

    public PedidoService(PedidoRepository pedidoRepository, CarrinhoRepository carrinhoRepository,
                         LojaService lojaService) {
        this.pedidoRepository = pedidoRepository;
        this.carrinhoRepository = carrinhoRepository;
        this.lojaService = lojaService;
    }

    public PedidoDTO.Response criar(Long clienteId, PedidoDTO.CriarRequest req) {
        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId)
                .filter(c -> !c.getItens().isEmpty())
                .orElseThrow(() -> new RegraNegocioException("O carrinho está vazio."));
        Cliente cliente = carrinho.getCliente();
        Loja loja = carrinho.getLoja();
        PedidoDTO.EntregaRequest dados = req.entrega();

        validarAgendamento(dados, loja);

        Pedido pedido = Pedido.novo(cliente, loja);
        for (ItemCarrinho item : carrinho.getItens()) {
            if (!item.getProduto().isAtivo()) {
                throw new RegraNegocioException("O produto \"" + item.getProduto().getNome()
                        + "\" não está mais disponível. Remova-o do carrinho.");
            }
            // snapshot do preço no momento da compra
            pedido.adicionarItem(new ItemPedido(item.getProduto(), item.getQuantidade(),
                    item.getProduto().getPreco(), item.getPersonalizacao()));
        }

        Endereco destino = null;
        if (dados.tipoRetirada() == TipoRetirada.ENTREGA) {
            destino = dados.endereco() != null ? dados.endereco().toEntity() : cliente.getEndereco();
            if (destino == null) {
                throw new RegraNegocioException("Informe o endereço de entrega.");
            }
        }
        Entrega entrega = new Entrega(pedido, dados.dataEntrega(), dados.horario(), dados.tipoRetirada(), destino);
        entrega.selecionarEntrega();
        pedido.definirEntrega(entrega);

        pedidoRepository.save(pedido);
        carrinho.limpar();
        return PedidoDTO.Response.from(pedido);
    }

    /** Cliente: seu histórico (RF014). Vendedor: pedidos da sua loja (RF013). Filtro de status opcional. */
    @Transactional(readOnly = true)
    public List<PedidoDTO.Response> listar(UsuarioAutenticado usuario, StatusPedido status) {
        List<Pedido> pedidos = switch (usuario.papel()) {
            case CLIENTE -> pedidoRepository.findByClienteIdOrderByDataPedidoDesc(usuario.id());
            case VENDEDOR -> {
                Loja loja = lojaService.buscarDoVendedor(usuario.id());
                yield pedidoRepository.findByLojaIdOrderByDataPedidoDesc(loja.getId());
            }
        };
        return pedidos.stream()
                .filter(p -> status == null || p.getStatus() == status)
                .map(PedidoDTO.Response::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PedidoDTO.Response buscar(UsuarioAutenticado usuario, Long pedidoId) {
        Pedido pedido = buscarEntidade(pedidoId);
        boolean donoCliente = pedido.getCliente().getId().equals(usuario.id());
        boolean donoLoja = pedido.getLoja().getVendedor().getId().equals(usuario.id());
        if (!donoCliente && !donoLoja) {
            throw new ProibidoException("Este pedido não pertence a você.");
        }
        return PedidoDTO.Response.from(pedido);
    }

    public PedidoDTO.Response atualizarStatus(Long vendedorId, Long pedidoId, StatusPedido novoStatus) {
        Pedido pedido = buscarEntidade(pedidoId);
        if (!pedido.getLoja().getVendedor().getId().equals(vendedorId)) {
            throw new ProibidoException("Este pedido pertence a outra loja.");
        }
        if (novoStatus == StatusPedido.CONFIRMADO) {
            throw new RegraNegocioException("A confirmação do pedido ocorre automaticamente com a aprovação do pagamento.");
        }
        pedido.atualizarStatus(novoStatus);
        return PedidoDTO.Response.from(pedido);
    }

    /** Cancela o pedido e, junto, a entrega associada (RF021). */
    public PedidoDTO.Response cancelar(Long clienteId, Long pedidoId) {
        Pedido pedido = buscarEntidade(pedidoId);
        if (!pedido.getCliente().getId().equals(clienteId)) {
            throw new ProibidoException("Este pedido não pertence a você.");
        }
        if (!pedido.getStatus().cancelavelPeloCliente()) {
            throw new RegraNegocioException("O pedido não pode mais ser cancelado (status atual: "
                    + pedido.getStatus() + ").");
        }
        pedido.cancelar();
        return PedidoDTO.Response.from(pedido);
    }

    Pedido buscarEntidade(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado."));
    }

    private void validarAgendamento(PedidoDTO.EntregaRequest dados, Loja loja) {
        if (LocalDateTime.of(dados.dataEntrega(), dados.horario()).isBefore(LocalDateTime.now())) {
            throw new RegraNegocioException("A data e o horário escolhidos já passaram.");
        }
        if (!loja.aceitaHorario(dados.horario())) {
            throw new RegraNegocioException("O horário escolhido está fora do funcionamento da loja ("
                    + loja.getAbertura() + " às " + loja.getFechamento() + ").");
        }
    }
}

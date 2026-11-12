package com.iflower.model.enums;

/**
 * Estados do pedido (RF010 / RF015) e suas transições válidas.
 * A regra de transição fica no próprio enum, evitando if/else espalhados.
 */
public enum StatusPedido {
    AGUARDANDO_PAGAMENTO,
    CONFIRMADO,
    EM_PREPARO,
    SAIU_PARA_ENTREGA,
    PRONTO_PARA_RETIRADA,
    ENTREGUE,
    CANCELADO;

    public boolean podeIrPara(StatusPedido destino) {
        return switch (this) {
            case AGUARDANDO_PAGAMENTO -> destino == CONFIRMADO || destino == CANCELADO;
            case CONFIRMADO -> destino == EM_PREPARO || destino == CANCELADO;
            case EM_PREPARO -> destino == SAIU_PARA_ENTREGA
                    || destino == PRONTO_PARA_RETIRADA
                    || destino == CANCELADO;
            case SAIU_PARA_ENTREGA, PRONTO_PARA_RETIRADA -> destino == ENTREGUE;
            case ENTREGUE, CANCELADO -> false;
        };
    }

    /** O cliente só pode cancelar antes de a loja iniciar o preparo. */
    public boolean cancelavelPeloCliente() {
        return this == AGUARDANDO_PAGAMENTO || this == CONFIRMADO;
    }
}

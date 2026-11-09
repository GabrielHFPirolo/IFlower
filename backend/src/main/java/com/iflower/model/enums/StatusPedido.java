package com.iflower.model.enums;

<<<<<<< HEAD
/**
 * Estados do pedido (RF010 / RF015) e suas transições válidas.
 * A regra de transição fica no próprio enum, evitando if/else espalhados.
 */
=======
>>>>>>> 30e1cb06bcc198f0b560a7b5897187a186905d87
public enum StatusPedido {
    AGUARDANDO_PAGAMENTO,
    CONFIRMADO,
    EM_PREPARO,
    SAIU_PARA_ENTREGA,
    PRONTO_PARA_RETIRADA,
    ENTREGUE,
    CANCELADO;

    public boolean podeIrPara(StatusPedido destino) {
<<<<<<< HEAD
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
=======
        switch (this) {
            case AGUARDANDO_PAGAMENTO:
                return destino == CONFIRMADO || destino == CANCELADO;

            case CONFIRMADO:
                return destino == EM_PREPARO || destino == CANCELADO;

            case EM_PREPARO:
                return destino == SAIU_PARA_ENTREGA
                        || destino == PRONTO_PARA_RETIRADA
                        || destino == CANCELADO;

            case SAIU_PARA_ENTREGA:
            case PRONTO_PARA_RETIRADA:
                return destino == ENTREGUE;

            case ENTREGUE:
            case CANCELADO:
                return false;

            default:
                return false;
        }
    }

    public boolean cancelavelPeloCliente() {
        return this == AGUARDANDO_PAGAMENTO
                || this == CONFIRMADO;
    }
}
>>>>>>> 30e1cb06bcc198f0b560a7b5897187a186905d87

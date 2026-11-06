package com.iflower.model.enums;

public enum StatusPedido {
    AGUARDANDO_PAGAMENTO,
    CONFIRMADO,
    EM_PREPARO,
    SAIU_PARA_ENTREGA,
    PRONTO_PARA_RETIRADA,
    ENTREGUE,
    CANCELADO;

    public boolean podeIrPara(StatusPedido destino) {
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
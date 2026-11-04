package com.iflower.model.enums;

/** Estados do pedido (RF010 / RF015). Ajustar conforme o diagrama de estados. */
public enum StatusPedido {
    AGUARDANDO_PAGAMENTO,
    CONFIRMADO,
    EM_PREPARO,
    SAIU_PARA_ENTREGA,
    PRONTO_PARA_RETIRADA,
    ENTREGUE,
    CANCELADO
}

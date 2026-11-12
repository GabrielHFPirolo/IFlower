package com.iflower.exception;

/** HTTP 403 - usuário autenticado sem permissão sobre o recurso. */
public class ProibidoException extends RuntimeException {
    public ProibidoException(String mensagem) {
        super(mensagem);
    }
}

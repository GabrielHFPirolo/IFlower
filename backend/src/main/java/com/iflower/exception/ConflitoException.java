package com.iflower.exception;

/** HTTP 409 - conflito com o estado atual (ex.: email já cadastrado). */
public class ConflitoException extends RuntimeException {
    public ConflitoException(String mensagem) {
        super(mensagem);
    }
}

package com.iflower.exception;

public class ProibidoException extends RuntimeException {
    public ProibidoException(String mensagem) {
        super(mensagem);
    }
}
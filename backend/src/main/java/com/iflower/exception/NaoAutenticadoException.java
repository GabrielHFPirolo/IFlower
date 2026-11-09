package com.iflower.exception;

/** HTTP 401 - credenciais inválidas. */
public class NaoAutenticadoException extends RuntimeException {
    public NaoAutenticadoException(String mensagem) {
        super(mensagem);
    }
}

package com.iflower.exception;

/** HTTP 422 - requisição válida, mas viola uma regra de negócio. */
public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}

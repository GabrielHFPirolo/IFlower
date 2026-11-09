package com.iflower.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/** Tratamento centralizado de erros: toda resposta de erro segue o formato de {@link ApiError}. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiError> naoEncontrado(RecursoNaoEncontradoException ex) {
        return resposta(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ApiError> regraNegocio(RegraNegocioException ex) {
        return resposta(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), null);
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ApiError> conflito(ConflitoException ex) {
        return resposta(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(ProibidoException.class)
    public ResponseEntity<ApiError> proibido(ProibidoException ex) {
        return resposta(HttpStatus.FORBIDDEN, ex.getMessage(), null);
    }

    @ExceptionHandler(NaoAutenticadoException.class)
    public ResponseEntity<ApiError> naoAutenticado(NaoAutenticadoException ex) {
        return resposta(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validacao(MethodArgumentNotValidException ex) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> campos.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return resposta(HttpStatus.BAD_REQUEST, "Dados inválidos.", campos);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> corpoInvalido(HttpMessageNotReadableException ex) {
        return resposta(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou malformado.", null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> tipoInvalido(MethodArgumentTypeMismatchException ex) {
        return resposta(HttpStatus.BAD_REQUEST, "Parâmetro inválido: " + ex.getName(), null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> integridade(DataIntegrityViolationException ex) {
        log.warn("Violação de integridade", ex);
        return resposta(HttpStatus.CONFLICT, "Operação viola uma restrição de integridade dos dados.", null);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> rotaInexistente(NoResourceFoundException ex) {
        return resposta(HttpStatus.NOT_FOUND, "Recurso não encontrado.", null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> metodoNaoSuportado(HttpRequestMethodNotSupportedException ex) {
        return resposta(HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP não suportado nesta rota.", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> inesperado(Exception ex) {
        log.error("Erro inesperado", ex);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor.", null);
    }

    private ResponseEntity<ApiError> resposta(HttpStatus status, String mensagem, Map<String, String> campos) {
        return ResponseEntity.status(status)
                .body(new ApiError(Instant.now(), status.value(), mensagem, campos));
    }
}

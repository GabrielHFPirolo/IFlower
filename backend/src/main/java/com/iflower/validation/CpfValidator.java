package com.iflower.validation;

import com.iflower.util.Documentos;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<Cpf, String> {
    @Override
    public boolean isValid(String valor, ConstraintValidatorContext contexto) {
        return valor == null || valor.isBlank() || Documentos.cpfValido(valor);
    }
}

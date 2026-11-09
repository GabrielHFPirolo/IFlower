package com.iflower.validation;

import com.iflower.util.Documentos;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CnpjValidator implements ConstraintValidator<Cnpj, String> {
    @Override
    public boolean isValid(String valor, ConstraintValidatorContext contexto) {
        return valor == null || valor.isBlank() || Documentos.cnpjValido(valor);
    }
}

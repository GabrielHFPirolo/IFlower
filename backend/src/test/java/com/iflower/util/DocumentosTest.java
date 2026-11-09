package com.iflower.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DocumentosTest {

    @Test
    void aceitaCpfValidoComOuSemMascara() {
        assertTrue(Documentos.cpfValido("529.982.247-25"));
        assertTrue(Documentos.cpfValido("52998224725"));
    }

    @Test
    void rejeitaCpfInvalido() {
        assertFalse(Documentos.cpfValido("529.982.247-24")); // dígito verificador errado
        assertFalse(Documentos.cpfValido("111.111.111-11")); // sequência repetida
        assertFalse(Documentos.cpfValido("123"));
        assertFalse(Documentos.cpfValido(null));
    }

    @Test
    void aceitaCnpjValidoComOuSemMascara() {
        assertTrue(Documentos.cnpjValido("11.222.333/0001-81"));
        assertTrue(Documentos.cnpjValido("11222333000181"));
    }

    @Test
    void rejeitaCnpjInvalido() {
        assertFalse(Documentos.cnpjValido("11.222.333/0001-80"));
        assertFalse(Documentos.cnpjValido("00000000000000"));
        assertFalse(Documentos.cnpjValido(null));
    }
}

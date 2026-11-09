package com.iflower.util;

/** Utilitários de CPF/CNPJ (RNF006 - integridade dos dados). */
public final class Documentos {

    private Documentos() {
    }

    public static String somenteDigitos(String valor) {
        return valor == null ? null : valor.replaceAll("\\D", "");
    }

    public static boolean cpfValido(String bruto) {
        String c = somenteDigitos(bruto);
        if (c == null || c.length() != 11 || c.chars().distinct().count() == 1) {
            return false;
        }
        return digitoCpf(c, 9) == c.charAt(9) - '0' && digitoCpf(c, 10) == c.charAt(10) - '0';
    }

    public static boolean cnpjValido(String bruto) {
        String c = somenteDigitos(bruto);
        if (c == null || c.length() != 14 || c.chars().distinct().count() == 1) {
            return false;
        }
        return digitoCnpj(c, 12) == c.charAt(12) - '0' && digitoCnpj(c, 13) == c.charAt(13) - '0';
    }

    private static int digitoCpf(String c, int quantidade) {
        int soma = 0;
        for (int i = 0; i < quantidade; i++) {
            soma += (c.charAt(i) - '0') * (quantidade + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }

    private static int digitoCnpj(String c, int quantidade) {
        int peso = quantidade - 7;
        int soma = 0;
        for (int i = 0; i < quantidade; i++) {
            soma += (c.charAt(i) - '0') * peso;
            peso--;
            if (peso < 2) {
                peso = 9;
            }
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}

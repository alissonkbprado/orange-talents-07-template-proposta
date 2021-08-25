package br.com.zup_academy.alisson_prado.proposta.compartilhado;

import java.util.Base64;

public class ConverteBase64 {

    private ConverteBase64() {
    }

    public static String decodifica(String valor) throws Exception {
            return new String(Base64.getDecoder().decode(valor));
    }

    public static String codifica(String valor) throws Exception {
            return Base64.getEncoder().encodeToString(valor.getBytes());
    }
}

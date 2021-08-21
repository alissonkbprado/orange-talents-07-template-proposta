package br.com.zup_academy.alisson_prado.proposta.compartilhado;

import java.util.UUID;

public class ValidaUuid {

    public static boolean isUuidValid(String uuid) {
        try {
            return UUID.fromString(uuid).toString().equals(uuid);
        } catch (Exception e){
            return false;
        }
    }

    public static boolean isUuidNotValid(String uuid) {
        try {
            return !UUID.fromString(uuid).toString().equals(uuid);
        } catch (Exception e){
            return true;
        }
    }
}

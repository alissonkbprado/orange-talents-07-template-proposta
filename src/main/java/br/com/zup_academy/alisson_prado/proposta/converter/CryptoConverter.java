package br.com.zup_academy.alisson_prado.proposta.converter;

import br.com.zup_academy.alisson_prado.proposta.exception.ApiErroException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.codec.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * Classe que converte um atributo (em modo texto ) de uma entidade que precisa ser
 * encriptada ao ser perisistida e desecriptada ao ser recuperada do banco de dados.
 *
 * A chave (key) para encriptar é recuperada do arquivo application.properties, que pode ser
 * atribuida via variável de ambiente.
 *
 * fonte: https://thorben-janssen.com/how-to-use-jpa-type-converter-to/
 *
 */
@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

    @Value("${encryption.key}")
    private String key;

    private static final String ALGORITMO = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    // initialization vector
    private static final byte[] IV = new byte[32];

    /**
     * Realiza a encriptação do dado e codifica para Base64
     * @param value
     * @return
     */
    @Override
    public String convertToDatabaseColumn(String value) {
        Key chave = new SecretKeySpec(this.key.getBytes(), "AES");
        try {
            Cipher c = Cipher.getInstance(ALGORITMO);
            c.init(Cipher.ENCRYPT_MODE, chave, new GCMParameterSpec(TAG_LENGTH_BIT, IV));
            return new String(Base64.encode(c.doFinal(value.getBytes())), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new ApiErroException(HttpStatus.INTERNAL_SERVER_ERROR, "Algorítimo de encriptação inválido.");
        } catch (InvalidKeyException e) {
            throw new ApiErroException(HttpStatus.INTERNAL_SERVER_ERROR, "Chave de encriptação inválida.");
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            throw new ApiErroException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao tentar Encriptar.");
        }
    }

    /**
     * Decodifica o dado e realiza a desencriptação
     * @param dbData
     * @return
     */
    @Override
    public String convertToEntityAttribute(String dbData) {
        Key chave = new SecretKeySpec(this.key.getBytes(), "AES");
        try {
            Cipher c = Cipher.getInstance(ALGORITMO);
            c.init(Cipher.DECRYPT_MODE, chave, new GCMParameterSpec(TAG_LENGTH_BIT, IV));
            return new String(c.doFinal(Base64.decode(dbData.getBytes())), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new ApiErroException(HttpStatus.INTERNAL_SERVER_ERROR, "Algorítimo de encriptação inválido.");
        } catch (InvalidKeyException e) {
            throw new ApiErroException(HttpStatus.INTERNAL_SERVER_ERROR, "Chave de encriptação inválida");
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            throw new ApiErroException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao tentar Desencriptar.");
        }
    }


}

package com.mycompany.tcp;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Base64;

public class CryptoUtils {
    private static final String AES_ALGORITHM = "AES";
    private static final String HASH_ALGORITHM = "SHA-256";

    // Método para cifrar un mensaje utilizando AES
    public  String encrypt(String message, String key) throws Exception {
        Key secretKey = new SecretKeySpec(generateHash(key), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Método para descifrar un mensaje utilizando AES
    public  String decrypt(String encryptedMessage, String key) throws Exception {
        Key secretKey = new SecretKeySpec(generateHash(key), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
        return new String(decryptedBytes);
    }

    // Método para generar un hash a partir de una clave
    private static byte[] generateHash(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        return digest.digest(input.getBytes());
    }
}
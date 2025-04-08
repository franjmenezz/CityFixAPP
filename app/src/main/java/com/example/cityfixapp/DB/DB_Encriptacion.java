package com.example.cityfixapp.DB;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;

public class DB_Encriptacion {
    private static final String ALGORITHM = "AES";
    private static final String KEY = "1234567890123456"; // Clave de 16 caracteres

    public static String encrypt(String data) throws Exception {
        Key key = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedValue = cipher.doFinal(data.getBytes());
        return Base64.encodeToString(encryptedValue, Base64.DEFAULT);
    }

    public static String decrypt(String data) throws Exception {
        Key key = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.decode(data, Base64.DEFAULT);
        byte[] decryptedValue = cipher.doFinal(decodedValue);
        return new String(decryptedValue);
    }



}
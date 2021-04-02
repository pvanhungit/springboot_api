package com.awesome.common.utils;

import com.awesome.common.Constants;
import org.springframework.util.Assert;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CipherUtil {
    private static final String ALGO = "AES";

    public static String encode(String text) throws Exception{
//        try{
            Assert.notNull(text, "Text must not be null");
            SecretKey key = generateKey();
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = cipher.doFinal(text.getBytes());
            return Base64.getUrlEncoder().encodeToString(encVal)
                    .replaceAll("=","");
//        }
//        catch (Exception e){
//            throw e;
//        }
    }

    public static String decode(String text) throws Exception{
        Assert.notNull(text, "Text must not be null");
        SecretKey key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue =
                Base64.getUrlDecoder().decode(text.getBytes());
        byte[] decValue = cipher.doFinal(decodedValue);
        return new String(decValue);
    }

    /** Generate a new encryption key. */
    private static SecretKey generateKey() {
        byte[] encryptionKeyBytes = Constants.CIPHER_SECRET_KEY.getBytes();
        return new SecretKeySpec(encryptionKeyBytes, ALGO);
    }
}

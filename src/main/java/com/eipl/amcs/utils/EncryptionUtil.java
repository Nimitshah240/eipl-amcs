package com.eipl.amcs.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDate;
import java.time.Period;
import java.util.Base64;

public class EncryptionUtil {
    public static String algo = "AES";
    public static String algoPadding = "AES/CBC/PKCS5Padding";
    public static String key = "1234567890123456";
    public static String iv = "1234567890123456";

    public static String encrypt(String message, String... key) {
        try {
            if (message.length() != 0)
                return Base64.getEncoder().encodeToString(encryptDecrypt(Cipher.ENCRYPT_MODE, message.getBytes("UTF-8"), key));
            else return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static String encrypt(String message) {
        try {
            if (message.length() != 0)
                return Base64.getEncoder().encodeToString(encryptDecrypt(Cipher.ENCRYPT_MODE, message.getBytes("UTF-8")));
            else return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static String decrypt(String message, String... key) {
        try {
            return new String(encryptDecrypt(Cipher.DECRYPT_MODE, Base64.getDecoder().decode(message), key), "UTF-8");
        } catch (Exception e) {
            return message;
        }
    }

    public static String decrypt(String message) {
        try {
            return new String(encryptDecrypt(Cipher.DECRYPT_MODE, Base64.getDecoder().decode(message)), "UTF-8");
        } catch (Exception e) {
            return message;
        }
    }

    public static String decryptDate(String message, String... key) {
        try {
            return new String(encryptDecrypt(Cipher.DECRYPT_MODE, Base64.getDecoder().decode(message), key), "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    public static String decryptDate(String message) {
        try {
            return new String(encryptDecrypt(Cipher.DECRYPT_MODE, Base64.getDecoder().decode(message)), "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] encryptDecrypt(final int mode, final byte[] message, String... key) throws Exception {
        final Cipher cipher = Cipher.getInstance(algoPadding, "SunJCE");
        final SecretKeySpec keySpec = new SecretKeySpec(key != null && key.length > 0 ? key[0].getBytes("UTF-8") : EncryptionUtil.key.getBytes("UTF-8"), algo);
        final IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
        cipher.init(mode, keySpec, ivSpec);
        return cipher.doFinal(message);
    }

    public static int getAgeFromMemberBirthDate(String birthDate) {
        try {
            LocalDate localDateBirthDate = LocalDate.parse(decrypt(birthDate), AppConstant.Formatter5);
            LocalDate curDate = LocalDate.now();
            return localDateBirthDate != null ? Period.between(localDateBirthDate, curDate).getYears() : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public static void main(String[] args) {
        System.out.println(EncryptionUtil.decrypt("kylk7MKPWSnvJzxACR4umg==").toString());
        System.out.println(EncryptionUtil.encrypt("01.01.2024").toString());
    }
}

package com.eipl.amcs.utils;

import com.eipl.amcs.MainApp;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDate;
import java.time.Period;
import java.util.Base64;

public class ActivationUtil {
    public static String algo = "AES";
    public static String algoPadding = "AES/CBC/PKCS5Padding";
    public static String key = "1234567890123456";
    public static String iv = "1234567890123456";

    public static String encrypt(String message, String... key) {
        try {
            if (message.length() != 0)
                return Base64.getEncoder()
                        .encodeToString(encryptDecrypt(Cipher.ENCRYPT_MODE, message.getBytes("UTF-8"), key));
            else
                return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static String encrypt(String message) {
        try {
            if (message.length() != 0)
                return Base64.getEncoder()
                        .encodeToString(encryptDecrypt(Cipher.ENCRYPT_MODE, message.getBytes("UTF-8")));
            else
                return null;
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
        final SecretKeySpec keySpec = new SecretKeySpec(
                key != null && key.length > 0 ? key[0].getBytes("UTF-8") : ActivationUtil.key.getBytes("UTF-8"), algo);
        final IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
        cipher.init(mode, keySpec, ivSpec);
        return cipher.doFinal(message);
    }

    public static int getAgeFromMemberBirthDate(String birthDate) {
        try {
            LocalDate localDateBirthDate = LocalDate.parse(decrypt(birthDate));
            LocalDate curDate = LocalDate.now();
            return localDateBirthDate != null ? Period.between(localDateBirthDate, curDate).getYears() : 0;
        } catch (Exception e) {
            return 0;
        }
    }

//    public static void main(String[] args) {
////		 System.out.println("decrypt...." +
////		 EncryptionUtil.decrypt("vbmlR9ZASvBUkV71SJpru7fSz+8EJo5ZWR3N6GUHh5U=").toString());//190509
////		 System.out.println("Mo: " +
////		 EncryptionUtil.decrypt("xXikUDRCOfLe2jpzJiZIvA==", "7659900188121659"));
//        System.out.println(encrypt("9194"));
//        // File file = new File("");
//        // System.out.println(file.getAbsolutePath());
//        // System.out.println("Host: " + encrypt("182.73.178.90"));
//        // System.out.println("user: " + encrypt("karan"));
//        // System.out.println("pass: " + encrypt("karan@123"));
//        // System.out.println("port: " + encrypt("12601"));
//        // System.out.println("Path: " + encrypt("/jarupdate/jar_1518030071.zip"));
//
//        System.out.println("Host: " + encrypt("182.74.63.142"));
//        System.out.println("user: " + encrypt("auratechmind"));
//        System.out.println("pass: " + encrypt("aura$2016$2017"));
//        System.out.println("port: " + encrypt("22"));
//        System.out.println("Path: " + encrypt("syncdata/"));
//
//        // System.out.println("Path: " + decrypt("xXikUDRCOfLe2jpzJiZIvA=="));
//
////		try {
////			List<String> list = Files.readAllLines(new File("mobile_no").toPath());
////			List<String> listNew = new ArrayList<>();
////			list.forEach(item -> {
////				listNew.add(decrypt(item));
////			});
////			Files.write(new File("mobile").toPath(), listNew);
////			System.out.println("Done...!");
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//
//        System.out.println(ActivationUtil.decrypt("Qjd8v3sgz9bDBxbZ1fb4Zg==").toString());
//    }
}

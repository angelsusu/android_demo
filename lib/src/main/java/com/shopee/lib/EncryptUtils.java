package com.shopee.lib;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * author: beitingsu
 * created on: 2021/9/2 3:59 下午
 */
class EncryptUtils {

    public static String encryptBookingInfo(String content, String key) {
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(new byte[16]);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);

            int length = 16 - content.length() % 16;
            StringBuilder stringBuilder = new StringBuilder(content);
            for (int i = 0; i < length; i++) {
                stringBuilder.append(" ");
            }
            content = stringBuilder.toString();

            byte[] bytes = content.getBytes("UTF-8");

            byte[] encrypted = cipher.doFinal(bytes);

            return byteToHex(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static String byteToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

}

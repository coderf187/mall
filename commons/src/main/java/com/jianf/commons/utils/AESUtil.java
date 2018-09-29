package com.jianf.commons.utils;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;


public class AESUtil {

    public static final String CHAR_ENCODING = "UTF-8";
    public static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 加密
     *
     * @param data
     * @param key
     * @return
     */
    public static byte[] encrypt(byte[] data, byte[] key) {
        notEmpty(data, "data");
        notEmpty(key, "key");
        if (key.length != 16) {
            throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            // 初始化
            cipher.init(Cipher.ENCRYPT_MODE, seckey);
            // 加密
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("encrypt fail!", e);
        }
    }


    /**
     * 解密
     *
     * @param data
     * @param key
     * @return
     */
    public static byte[] decrypt(byte[] data, byte[] key) {
        notEmpty(data, "data");
        notEmpty(key, "key");
        if (key.length != 16) {
            throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, seckey);
            // 加密
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("decrypt fail!", e);
        }
    }

    /**
     * 加密base64
     *
     * @param source
     * @param key
     * @return
     */
    public static String encryptToBase64(String source, String key) {
        try {
            byte[] valueByte = encrypt(source.getBytes(CHAR_ENCODING),
                    key.getBytes(CHAR_ENCODING));
            return new String(new BASE64Encoder().encodeBuffer(valueByte));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("encryptToBase64 fail!", e);
        }

    }

    /**
     * 解密base64
     *
     * @param cryptograph
     * @param key
     * @return
     */
    public static String decryptFromBase64(String cryptograph, String key) {
        try {
            byte[] originalData = new BASE64Decoder().decodeBuffer(cryptograph);
            byte[] valueByte = decrypt(originalData, key.getBytes(CHAR_ENCODING));
            return new String(valueByte, CHAR_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("decryptFromBase64 fail!", e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证对象是否为NULL,空字符串，空数组，空的Collection或Map(只有空格的字符串也认为是空串)
     *
     * @param obj     被验证的对象
     * @param message 异常信息
     */
    public static void notEmpty(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message + " must be specified");
        }
        if (obj instanceof String && obj.toString().trim().length() == 0) {
            throw new IllegalArgumentException(message + " must be specified");
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            throw new IllegalArgumentException(message + " must be specified");
        }
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            throw new IllegalArgumentException(message + " must be specified");
        }
        if (obj instanceof Map && ((Map) obj).isEmpty()) {
            throw new IllegalArgumentException(message + " must be specified");
        }
    }

}
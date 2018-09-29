package com.jianf.commons.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * token加解密工具类
 */
public class TokenUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenUtils.class);
    //对称密码 1121334455667788
    public static final String AES_PASSWORD_TOKEN = "H5AES123456!@#$%";//NOSONAR


    public static void main(String[] args) {
        String token = generateToken("1");
        LOGGER.info(token);
        LOGGER.info("id: {}", getUID(token));
        LOGGER.info("tms: {}", getTimeMs(token));
    }

    /**
     * 生成加密token
     */
    public static String generateToken(String uid) {
        String content = uid + "_" + System.currentTimeMillis();
        return encrypt(content);
    }

    /**
     * 通过加密token获取UID
     */
    public static long getUID(String encryptToken) {
        if (StringUtils.isEmpty(encryptToken)) {
            return 0;
        }
        String token = decrypt(encryptToken);
        token = StringUtils.substringBefore(token, "_");
        return Long.valueOf(token);
    }

    /**
     * 通过加密token获得时间ms
     */
    public static long getTimeMs(String encryptToken){
        if (StringUtils.isEmpty(encryptToken)) {
            return 0;
        }
        String token = decrypt(encryptToken);
        token = StringUtils.substringAfter(token, "_");
        return Long.valueOf(token);
    }


    /**
     * token加密操作,使用aes对称加密32位算法,注意对称密码必须是16的倍数
     */
    private static String encrypt(String str) {
        try {
            byte[] kb = AES_PASSWORD_TOKEN.getBytes("utf-8");
            SecretKeySpec sks = new SecretKeySpec(kb, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//算法/模式/补码方式
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            byte[] eb = cipher.doFinal(str.getBytes("utf-8"));
            return Base64.getEncoder().encodeToString(eb);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密操作
     */
    private static String decrypt(String str) {
        try {
            byte[] kb = AES_PASSWORD_TOKEN.getBytes("utf-8");
            SecretKeySpec sks = new SecretKeySpec(kb, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, sks);
            byte[] by = Base64.getDecoder().decode(str);
            byte[] db = cipher.doFinal(by);
            return new String(db);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

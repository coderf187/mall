package com.jianf.commons.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Created by Puhui on 2017/6/2.
 */
public class AesVideoUtils {

    private static final String AES_KEY = "FG_2017_ly_3.0t%";
    private static final String IV_STRING = "FinUpGroup_2017%";

    public static String encryptAES(String content) {
        String result = "";
        try {
            byte[] encryptedBytes;
            Base64.Encoder encoder;
            byte[] byteContent = content.getBytes("UTF-8");
            // 注意，为了能与 iOS 统一
            // 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
            byte[] enCodeFormat = AES_KEY.getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            byte[] initParam = IV_STRING.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            // 指定加密的算法、工作模式和填充方式
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            encryptedBytes = cipher.doFinal(byteContent);
            // 同样对加密后数据进行 base64 编码
            encoder = Base64.getEncoder();
            result = encoder.encodeToString(encryptedBytes);
            result = byteToHexString(result.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String decryptAES(String content) {
        String result = "";
        try {
            byte[] resultByte;
            // base64 解码
            String tmp  = new String(hexStringToByte(content), "UTF-8");
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encryptedBytes = decoder.decode(tmp);
            byte[] enCodeFormat = AES_KEY.getBytes();
            SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");
            byte[] initParam = IV_STRING.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            resultByte = cipher.doFinal(encryptedBytes);
            result = new String(resultByte, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String byteToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        byte[] var2 = bytes;
        int var3 = bytes.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            String strHex = Integer.toHexString(b);
            if(strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else if(strHex.length() < 2) {
                sb.append("0").append(strHex);
            } else {
                sb.append(strHex);
            }
        }
        return sb.toString();
    }

    private static byte[] hexStringToByte(String s) {
        byte[] baKeyword = new byte[s.length() / 2];

        for(int i = 0; i < baKeyword.length; ++i) {
            try {
                baKeyword[i] = (byte)(255 & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

        return baKeyword;
    }
}

package com.jianf.commons.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


public class RSAUtil {
    /**
     * 指定加密算法为RSA
     */
    public static final String CHAR_ENCODING = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";

    public static void main(String[] args) throws Exception {
        String encryptKey = "Rr+lyTuvM8LFhDZrF34VglGZOliWjymx9THAYHZ5pArqE+SNcKuVr9bBhzPVrEgXMRIpJYaxgSCM6uRccPnQujIbw4BkTZZ1qxESYz8553OnIWLtKbYJtdVoiBNRMVDd2/xonhm04aU4lFE7+Ld+e7IPcgrwQCkDn0eXyX/14AU=";
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJqqvcBu6ZrczyzQ2nuON7Tvz13Sve2UroIEDUj/EpZBiBLUuNdRWfI1+M6D/Jo4dTjLNKnTeeeVUKnvMQ2yCvhhono2C1EVi8zPbYRqvqlPhprvMgsZOTTQ2RBmy0d00F2rQG89fQDgHE7FVfqW3fI9tbeu9zsWSF2oo3WcBwH5AgMBAAECgYBmjutuK92pe9vA6ujFX0OfLhmCLFkWri2dNxScH89yZRfW7Ml4tZAAJTvLrQeWN/O66jwMlJdUMdWgKWSMoU/V5fszd9DGGvT8bFotl54838Z2XBzZ9XYeyh3YNWpB+IWG+NMlo1PTqdcDovC7LDycdZb4Ed9hHWkyDPHTG82zgQJBAMc7xz+kUt/wtloaQIb2tg1cQIs9X7Gc6l4XWBjZavW/XdtbRfrV6PleiEJXTDCY09tYR7IcVxtYgxj9IHkgOnECQQDGvD/rXgWLjZ/0khR5q2x2l6DM/qp74Wy51RTTOqWSMnHdBPOXdgK2u4c4aIKf4lMTtRoF5Rf37kW0IfhVZjQJAkAWShXMhPHQo/mCsxUtBETx0bRW35LpKAkAZdQxU4Gn5LT3aYq2uV/LJkxQP7wCY9av33yF7K3CHXDtRsRvSK/BAkBfouPXrk1Y1PSuy0WTL41onJtDSyJbryWVYBc/dy+KZUAqLmHS93Vi3me6G4EQ0+dKypIVWCPqqf1dLCPHZQGRAkAk+QRSsVwZLYFn9SUs0MkyvZfolM3ItKsrPrywwTBzjL/MZjbOet2TZPdysPsD6JYKSDAXbrLlUv3/lrbnAMq5";
        String rdm = decrypt(encryptKey, privateKey);
        System.out.println(rdm);

        String dateEnc = "d+Oadmmi2zuM93mjLMLRs/ADy/1hY6J9MrSFCI6EJuV49egqIoVFS7BHDXszNDda5tlze+pUaEahN5xnDibkHI6+imxOJh2jih3gAU7I4yTu/fop8rlWyRD0DDWP/gXgP99auVD9cw7DNe6zVP9DWkWi34aD3i/CqUPvW/iYlPVnsVIjniIwUk05hRrMJaPYIKZ2hUeE7v11ZyFs5iwznwex4WH+b4jTl1jrUd/6C3xF2276SlZVicJt+bORGHUtMMHKljK3icgeOqhA0lJ+mC7sZ8xL3+DgSy0a1Z7j5ulgwD611sGvfN9XckFWbKf8TkIi30vWdVk6JVK0wPvfie3dsk9Wlt498vOgtjFcKp/8e10K0rCDaYXiMxbvJj9Cj/kkZB4ECppY6PRnle6xH8tHogQ84enyPPPl+VVcCzx7KKY0mkfHC6foh2OT39o3kSYYhpoqUbag9yrLhYqqELyiYq9Eyb2qgS1Gd+oqmif64m0mWCHSKoyx6Rjpj7N0reESCF9PxrElIKjpm00MZ5vG1TqiXCWC0V5H1fGDLA0JKdI3NvZe1GCGA4db1z3lPB0THtkO8+QkusLu6wfRODUzp+Of9ailtl6o3xpT+2U=";
        String res = AESUtil.decryptFromBase64(dateEnc, rdm);
        System.out.println(res);
    }

    /**
     * 指定key的大小
     */
    private static int KEY_SIZE = 1024;

    /**
     * 生成密钥对算法
     *
     * @return
     * @throws Exception
     */
    public static Map<String, String> generateKeyPair() throws Exception {
        /** RSA算法要求有一个可信任的随机数源 */
        SecureRandom sr = new SecureRandom();
        /** 为RSA算法创建一个KeyPairGenerator对象 */
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
        kpg.initialize(KEY_SIZE, sr);
        /** 生成密匙对 */
        KeyPair kp = kpg.generateKeyPair();
        /** 得到公钥 */
        Key publicKey = kp.getPublic();
        byte[] publicKeyBytes = publicKey.getEncoded();

        String publicKeyBase64 = new BASE64Encoder().encode(publicKeyBytes);
        System.out.println("publicKeyBase64 = " + publicKeyBase64 + "");

        /** 得到私钥 */
        Key privateKey = kp.getPrivate();
        byte[] privateKeyBytes = privateKey.getEncoded();

        String privateKeyBase64 = new BASE64Encoder().encode(privateKeyBytes);
        System.out.println("privateKeyBase64 = " + privateKeyBase64 + "");

        Map<String, String> map = new HashMap<String, String>();
        map.put("publicKey", publicKeyBase64);
        map.put("privateKey", privateKeyBase64);

        return map;
    }

    /**
     * 公钥加密算法--->加密
     *
     * @param source    数据源
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static String encrypt(String source, String publicKey) throws Exception {
        Key key = getPublicKey(publicKey);
        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] b = source.getBytes();
        /** 执行加密操作 */
        byte[] b1 = cipher.doFinal(b);
        return new String(new BASE64Encoder().encode(b1));
    }

    /**
     * 私钥解密算法--->解密
     *
     * @param cryptograph 密文
     * @param privateKey  私钥
     * @return
     * @throws Exception
     */
    public static String decrypt(String cryptograph, String privateKey) throws Exception {
        Key key = getPrivateKey(privateKey);
        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] b1 = new BASE64Decoder().decodeBuffer(cryptograph);
        /** 执行解密操作 */
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }

    /**
     * 获取公钥
     *
     * @param key 密钥字符串（经过base64编码）
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(new BASE64Decoder().decodeBuffer(key));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 获取私钥
     *
     * @param key 密钥字符串（经过base64编码）
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(key));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 根据私钥进行数据签名
     *
     * @param content
     * @param privateKey
     * @return
     */
    public static String sign(String content, String privateKey) {
        String charset = CHAR_ENCODING;
        try {
            PrivateKey priKey = getPrivateKey(privateKey);

            Signature signature = Signature.getInstance("SHA1WithRSA");

            signature.initSign(priKey);
            signature.update(content.getBytes(charset));

            byte[] signed = signature.sign();

            return new String(new BASE64Encoder().encode(signed));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据公钥进行签名验签
     *
     * @param content
     * @param sign
     * @param publicKey
     * @return
     */
    public static boolean checkSign(String content, String sign, String publicKey) {
        try {
            PublicKey pubKey = getPublicKey(publicKey);

            Signature signature = Signature.getInstance("SHA1WithRSA");

            signature.initVerify(pubKey);
            signature.update(content.getBytes("UTF-8"));

            boolean bverify = signature.verify(new BASE64Decoder().decodeBuffer(sign));
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

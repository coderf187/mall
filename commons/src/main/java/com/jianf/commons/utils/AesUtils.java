package com.jianf.commons.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by fengjian on 2016/10/25.
 */
public class AesUtils {

    private AesUtils() {

    }

    /**
     * 加密方法返回值会有“xy”前缀
     * 
     * @param content
     * @return
     */
    public static String encrypt(String content) {
        if (StringUtils.isEmpty(content)) {
            return content;
        }
        // 防止重复加密
        if (StringUtils.startsWithIgnoreCase(content, "xy")) {
            return content;
        }
        return AesEncryptionUtil.encrypt(content);
    }

    /**
     * 解密 解密方法需要传入带有“xy”前缀的字符串，才能解密；如果没有前缀则不会处理，直接返回原始字符串
     * 
     * @return
     */
    public static String decrypt(String content) {
        if (StringUtils.isEmpty(content)) {
            return content;
        }
        return AesEncryptionUtil.decrypt(content);
    }

}

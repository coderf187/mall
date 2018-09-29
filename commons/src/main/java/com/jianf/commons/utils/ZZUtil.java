package com.jianf.commons.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 正则工具类
 */
public class ZZUtil {

    public static void main(String[] args) {
        System.out.println(ZZUtil.valid(ZZUtil.REGEX_EMAIL, "你好bingac@bupt@xxfoxmail.xom"));
    }

    public static final String NUMBER = "^[1-9][0-9]*$";
    public static final String ZZ_BANK = "(\\d{5,30})";
    public static final String REGEX_MOBILE = "^[1][34578][0-9]{9}$";
    //^([a-z0-9A-Z]+[-|\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\.)+[a-zA-Z]{2,}$
    public static final String REGEX_EMAIL = "^(\\S+)@(\\S+)\\.(\\S{2,})$";
    /**
     * 正则表达式：验证用户名
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";
    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";
    /**
     * 正则表达式：验证金额
     */
    public static final String REGEX_AMOUNT = "^[1-9]\\d*(\\.\\d+)?$";

    public static Map<String, String> paramsToMap(String urlParams){
        Map<String, String> reqMap = new HashMap<>();
        if(StringUtils.isEmpty(urlParams)){
            return reqMap;
        }
        String[] arr = urlParams.split("&");
        for(String str: arr){
            String[] kv = str.split("=");
            if(kv.length > 1){
                reqMap.put(kv[0], kv[1]);
            }
        }
        return reqMap;
    }

    /**
     * str或zz为null，返回 false
     */
    public static boolean valid(String zz, String str){
        if(StringUtils.isEmpty(str) || StringUtils.isEmpty(zz)){
            return false;
        }

        return Pattern.compile(zz).matcher(str).matches();
    }

}

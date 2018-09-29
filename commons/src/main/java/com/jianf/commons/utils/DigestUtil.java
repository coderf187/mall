package com.jianf.commons.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 快捷支付加密算法
 */
public class DigestUtil {

    /**
     * 该方法返回MD5加密后的一个String
     */
    public static String encrpty(Map<String, Object> params,String salt) throws Exception {
        Map<String, Object> sortedParams = new LinkedHashMap<>();
        params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> sortedParams.put(x.getKey(), x.getValue()));
        StringBuilder sb = new StringBuilder();
        sb.append(salt);
        Iterator iter = sortedParams.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (null != entry.getValue()) {
                sb.append(entry.getKey());
                sb.append(entry.getValue());
            }
        }
        sb.append(salt);
        String sign = encryptParam(sb.toString());
        return sign;
    }

    /**
     * 该方法返回MD5加密后的一个String
     */
    public static String encrpty(Object dto,String salt) throws Exception {
        Map<String, Object> params = transBean2Map(dto);
        return encrpty(params,salt);
    }

    /**
     * Bean --> Map: 利用Introspector和PropertyDescriptor
     *
     * @param obj
     * @return
     */
    private static Map<String, Object> transBean2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }
        return map;
    }

    /**
     * MD5 加密
     *
     * @param param
     * @return
     * @throws Exception
     */
    private static String encryptParam(String param) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(param.getBytes("UTF-8"));
        byte[] digest = md.digest();
        return byteToHexString(digest);
    }

    private static String byteToHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }

    public static void main(String[] args) throws Exception {
        Map map = new HashMap();
        map.put("key001","v001");
        map.put("key003","v003");
        map.put("key002","v002");
        String str = DigestUtil.encrpty(null,"123");
        System.out.println(str);
    }
}

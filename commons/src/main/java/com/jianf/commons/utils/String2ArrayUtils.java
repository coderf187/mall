package com.jianf.commons.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by fengjian on 2016/9/8.
 */
public class String2ArrayUtils {

    private String2ArrayUtils() {
    }

    public static String convertMapToString(Map map){
        Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        for(Iterator iterator = map.entrySet().iterator(); iterator.hasNext();){
            entry = (Map.Entry)iterator.next();
            sb.append(entry.getKey().toString()).append( " - " ).append(null==entry.getValue()? "":
                    entry.getValue().toString()).append (iterator.hasNext() ? "; " : "");
        }
        return sb.toString();
    }

    /**
     * 数组转换string fengjian
     *
     * @param ig
     * @return
     */
    public static String converToString(String[] ig) {
        StringBuilder  stringBuilder= new StringBuilder();
        if (ig != null && ig.length > 0) {
            for (String anIg : ig) {
                stringBuilder.append(anIg).append(",");
            }
        }
        String s=stringBuilder.toString();
        s = s.substring(0, s.length() - 1);
        return s;
    }

    /**
     * list转换string fengjian
     *
     * @param list
     * @return
     */
    public static String listToString(List list) {
        StringBuilder sb = new StringBuilder();
        if (CollectionUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i)).append(",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

    public static List<String> stringToList(String s) {
        List<String> list = new ArrayList<>();
        if (StringUtils.isNotBlank(s)) {
            int index = StringUtils.indexOf(s, ",");
            if (index > 0) {
                String[] strings = StringUtils.split(s, ",");
                Collections.addAll(list, strings);
            } else {
                list.add(s);
            }
        }
        return list;
    }

    /**
     * 检验source 是否在 target 字符数组里面
     * @param source 待检验对象
     * @param target 待检验字符池
     * @return 是否 in 字符池
     */
    public static boolean in(String source, String[] target) {
        if (source == null || ArrayUtils.isEmpty(target)) {
            throw new IllegalStateException("请检查输入参数是否为空");
        }
        for (String s : target) {
            if (Objects.equals(source, s)) {
                return true;
            }
        }
        return false;
    }
}
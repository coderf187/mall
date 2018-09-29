package com.jianf.commons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fengjian on 2017/2/21.
 */
public class HumpUtils {

    private static final Pattern linePattern = Pattern.compile("_(\\w)");

    private static final Pattern humpPattern = Pattern.compile("[A-Z]");

    /** 下划线转驼峰 */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /** 驼峰转下划线,效率比上面高 */
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
